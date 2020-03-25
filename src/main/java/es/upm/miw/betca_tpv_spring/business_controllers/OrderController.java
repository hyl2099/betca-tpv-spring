package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.documents.Provider;
import es.upm.miw.betca_tpv_spring.dtos.OrderCreationDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderLineDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderSearchDto;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class OrderController {

    private OrderReactRepository orderReactRepository;

    private ProviderRepository providerRepository;

    private ArticleRepository articleRepository;

    private ArticleReactRepository articleReactRepository;

    private ProviderReactRepository providerReactRepository;

    @Autowired
    public OrderController(OrderReactRepository orderReactRepository, ProviderRepository providerRepository, ArticleRepository articleRepository,
                           ArticleReactRepository articleReactRepository, ProviderReactRepository providerReactRepository) {
        this.orderReactRepository = orderReactRepository;
        this.providerRepository = providerRepository;
        this.articleRepository = articleRepository;
        this.articleReactRepository = articleReactRepository;
        this.providerReactRepository = providerReactRepository;
    }

    public Flux<OrderDto> searchOrder(OrderSearchDto orderSearchDto) {
        if (orderSearchDto.getClosingDate().equals("null")) {
            if (orderSearchDto.getDescription().equals("null") && orderSearchDto.getProviderId().equals("null")) {
                return this.orderReactRepository.findAll()
                        .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                        .filter(order -> order.getClosingDate() == null)
                        .map(OrderDto::new);
            }
            return this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), orderSearchDto.getProviderId())
                    .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                    .filter(order -> order.getClosingDate() == null)
                    .map(OrderDto::new);
        } else {
            if (orderSearchDto.getDescription().equals("null") && orderSearchDto.getProviderId().equals("null")) {
                return this.orderReactRepository.findAll()
                        .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                        .filter(order -> order.getClosingDate() != null)
                        .map(OrderDto::new);
            }
            return this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), orderSearchDto.getProviderId())
                    .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                    .filter(order -> order.getClosingDate() != null)
                    .map(OrderDto::new);
        }
    }

    public Mono<OrderDto> createOrder(OrderCreationDto orderCreationDto) {
        Mono<Void> provider;
        Order order = new Order(orderCreationDto.getDescription(), null, null);
        if (orderCreationDto.getProviderId() == null) {
            provider = Mono.empty();
        } else {
            provider = this.providerReactRepository.findById(orderCreationDto.getProviderId())
                    .switchIfEmpty(Mono.error(new NotFoundException("Provider (" + orderCreationDto.getProviderId() + ")")))
                    .doOnNext(order::setProvider).then();
        }
        OrderLine[] orderLines = new OrderLine[orderCreationDto.getOrderLines().length];
        String articleId;
        for (int i = 0; i < orderCreationDto.getOrderLines().length; i++) {
            articleId = orderCreationDto.getOrderLines()[i].getArticleId();
            orderLines[i] = new OrderLine(this.articleRepository.findById(articleId).get(), orderCreationDto.getOrderLines()[i].getRequiredAmount());
        }
        order.setOrderLines(orderLines);
        return Mono.when(provider).then(this.orderReactRepository.save(order)).map(OrderDto::new);
    }

    public Mono<Void> deleteOrder(String orderId) {
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id:" + orderId)));
        return Mono
                .when(order)
                .then(this.orderReactRepository.deleteById(orderId));
    }

    public Mono<OrderDto> updateOrder(String orderId, OrderDto orderDto) {
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id: " + orderId)))
                .map(orderData -> {
                    orderData.setDescription(orderDto.getDescription());
                    OrderLine[] orderLines = new OrderLine[orderDto.getOrderLines().length];
                    for (int i = 0; i < orderDto.getOrderLines().length; i++) {
                        OrderLineDto orderLineDto = orderDto.getOrderLines()[i];
                        Article article = this.articleRepository.findById(orderLineDto.getArticle()).get();
                        orderLines[i] = new OrderLine(article, orderLineDto.getRequiredAmount());
                    }
                    orderData.setOrderLines(orderLines);
                    return orderData;
                });
        return Mono
                .when(order)
                .then(this.orderReactRepository.saveAll(order).next())
                .map(OrderDto::new);
    }

    public Mono<OrderDto> getOrder(String orderId) {
        return this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id: " + orderId))).map(OrderDto::new);
    }

    public Mono<OrderDto> closeOrder(String orderId, OrderDto orderDto) {
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id: " + orderId)))
                .map(orderToClose -> {
                    OrderLine[] orderLines = new OrderLine[orderDto.getOrderLines().length];
                    for (int i = 0; i < orderDto.getOrderLines().length; i++) {
                        OrderLineDto orderLineDto = orderDto.getOrderLines()[i];
                        Article article = this.articleRepository.findById(orderLineDto.getArticle()).get();
                        orderLines[i] = new OrderLine(article, orderLineDto.getRequiredAmount());
                        orderLines[i].setFinalAmount(orderLineDto.getFinalAmount());
                    }
                    orderToClose.setOrderLines(orderLines);
                    orderToClose.close();
                    return orderToClose;
                });
        Mono.when(order).then(updateArticlesStockAssured(orderDto));
        return this.orderReactRepository.saveAll(order).next().map(OrderDto::new);
    }

    private Mono<Void> updateArticlesStockAssured(OrderDto orderDto) {
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineDto.getArticle())
                    .switchIfEmpty(Mono.error(new NotFoundException("Article (" + orderLineDto.getArticle() + ")")))
                    .map(article -> {
                        article.setStock(article.getStock() + orderLineDto.getFinalAmount());
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(this.articleReactRepository.saveAll(articleReact));
        }
        return articlesFlux.then();
    }
}
