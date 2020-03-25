package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.documents.Provider;
import es.upm.miw.betca_tpv_spring.dtos.*;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
        Flux<OrderDto> orderDtoFlux;
        if (orderSearchDto.getClosingDate().equals("null")) {
            if (orderSearchDto.getDescription().equals("null") && orderSearchDto.getProviderId().equals("null")) {
                orderDtoFlux = this.orderReactRepository.findAll()
                        .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                        .map(OrderDto::new);
            } else {
                orderDtoFlux = this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), orderSearchDto.getProviderId())
                        .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                        .map(OrderDto::new);
            }
            return orderDtoFlux.filter(order -> order.getClosingDate() == null);
        } else {
            if (orderSearchDto.getDescription().equals("null") && orderSearchDto.getProviderId().equals("null")) {
                orderDtoFlux = this.orderReactRepository.findAll()
                        .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                        .map(OrderDto::new);
            } else {
                orderDtoFlux = this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), orderSearchDto.getProviderId())
                        .switchIfEmpty(Flux.error(new NotFoundException("Nothing found")))
                        .map(OrderDto::new);
            }
            return orderDtoFlux.filter(order -> order.getClosingDate() != null);
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
        List<OrderLine> orderLineList = new ArrayList<>();
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineCreationDto orderLineCreationDto : orderCreationDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineCreationDto.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException("Article (" + orderLineCreationDto.getArticleId() + ")")))
                    .map(article -> {
                        orderLineList.add(new OrderLine(article, orderLineCreationDto.getRequiredAmount()));
                        order.setOrderLines(orderLineList.toArray(new OrderLine[orderLineList.size()]));
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(articleReact);
        }
        return Mono.when(provider, articlesFlux).then(this.orderReactRepository.save(order)).map(OrderDto::new);
    }

    public Mono<Void> deleteOrder(String orderId) {
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id:" + orderId)));
        return Mono
                .when(order)
                .then(this.orderReactRepository.deleteById(orderId));
    }

    public Mono<OrderDto> updateOrder(String orderId, OrderDto orderDto) {
        List<OrderLine> orderLineList = new ArrayList<>();
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineDto.getArticle())
                    .switchIfEmpty(Mono.error(new NotFoundException("Article (" + orderLineDto.getArticle() + ")")))
                    .map(article -> {
                        orderLineList.add(new OrderLine(article, orderLineDto.getRequiredAmount()));
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(articleReact);
        }
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id: " + orderId)))
                .map(orderData -> {
                    orderData.setDescription(orderDto.getDescription());
                    orderData.setOrderLines(orderLineList.toArray(new OrderLine[orderLineList.size()]));
                    return orderData;
                });
        return Mono.when(articlesFlux).then(order).then(this.orderReactRepository.saveAll(order).next()).map(OrderDto::new);
    }

    public Mono<OrderDto> getOrder(String orderId) {
        return this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id: " + orderId))).map(OrderDto::new);
    }

    public Mono<OrderDto> closeOrder(String orderId, OrderDto orderDto) {
        List<OrderLine> orderLineList = new ArrayList<>();
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineDto.getArticle())
                    .switchIfEmpty(Mono.error(new NotFoundException("Article (" + orderLineDto.getArticle() + ")")))
                    .map(article -> {
                        OrderLine orderLine = new OrderLine(article, orderLineDto.getRequiredAmount());
                        orderLineDto.setFinalAmount(orderLineDto.getFinalAmount());
                        orderLineList.add(orderLine);
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(articleReact);
        }
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException("Order id: " + orderId)))
                .map(orderToClose -> {
                    orderToClose.setOrderLines(orderLineList.toArray(new OrderLine[orderLineList.size()]));
                    orderToClose.close();
                    return orderToClose;
                });
        return Mono.when(articlesFlux).then(order).then(updateArticlesStockAssured(orderDto))
                .then(this.orderReactRepository.saveAll(order).next()).map(OrderDto::new);
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
