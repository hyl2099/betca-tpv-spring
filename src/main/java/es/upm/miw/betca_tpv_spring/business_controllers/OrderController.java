package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Article;
import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.dtos.*;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
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

    private ArticleReactRepository articleReactRepository;

    private ProviderReactRepository providerReactRepository;

    private static final String NOTHING_FOUND = "Nothing found";

    private static final String ORDER_ID_NOT_FOUND = "Order id: ";

    private static final String ARTICLE_NOT_FOUND = "Article: ";

    private static final String PROVIDER_NOT_FOUND = "Provider: ";

    @Autowired
    public OrderController(OrderReactRepository orderReactRepository, ArticleReactRepository articleReactRepository,
                           ProviderReactRepository providerReactRepository) {
        this.orderReactRepository = orderReactRepository;
        this.articleReactRepository = articleReactRepository;
        this.providerReactRepository = providerReactRepository;
    }

    public Flux<OrderDto> searchOrder(OrderSearchDto orderSearchDto) {
        Flux<OrderDto> orderDtoFlux;
        if (orderSearchDto.getClosingDate().equals("null")) {
            if (orderSearchDto.getDescription().equals("null") && orderSearchDto.getProviderId().equals("null")) {
                orderDtoFlux = this.orderReactRepository.findAll()
                        .switchIfEmpty(Flux.error(new NotFoundException(NOTHING_FOUND)))
                        .map(OrderDto::new);
            } else {
                orderDtoFlux = this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), orderSearchDto.getProviderId())
                        .switchIfEmpty(Flux.error(new NotFoundException(NOTHING_FOUND)))
                        .map(OrderDto::new);
            }
            return orderDtoFlux.filter(order -> order.getClosingDate() == null);
        } else {
            if (orderSearchDto.getDescription().equals("null") && orderSearchDto.getProviderId().equals("null")) {
                orderDtoFlux = this.orderReactRepository.findAll()
                        .switchIfEmpty(Flux.error(new NotFoundException(NOTHING_FOUND)))
                        .map(OrderDto::new);
            } else {
                orderDtoFlux = this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), orderSearchDto.getProviderId())
                        .switchIfEmpty(Flux.error(new NotFoundException(NOTHING_FOUND)))
                        .map(OrderDto::new);
            }
            return orderDtoFlux.filter(order -> order.getClosingDate() != null);
        }
    }

    public Mono<OrderDto> createOrder(OrderCreationDto orderCreationDto) {
        Mono<Void> provider;
        Order order = new Order(orderCreationDto.getDescription(), null, null);
        if (orderCreationDto.getProviderId() == null) {
            provider = Mono.error(new BadRequestException("Provider can't be null"));
        } else {
            provider = this.providerReactRepository.findById(orderCreationDto.getProviderId())
                    .switchIfEmpty(Mono.error(new NotFoundException(PROVIDER_NOT_FOUND + orderCreationDto.getProviderId())))
                    .doOnNext(order::setProvider).then();
        }
        List<OrderLine> orderLineList = new ArrayList<>();
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineCreationDto orderLineCreationDto : orderCreationDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineCreationDto.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException(ARTICLE_NOT_FOUND + orderLineCreationDto.getArticleId())))
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
                .switchIfEmpty(Mono.error(new NotFoundException(ORDER_ID_NOT_FOUND + orderId)));
        return Mono
                .when(order)
                .then(this.orderReactRepository.deleteById(orderId));
    }

    public Mono<OrderDto> updateOrder(String orderId, OrderDto orderDto) {
        List<OrderLine> orderLineList = new ArrayList<>();
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineDto.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException(ARTICLE_NOT_FOUND + orderLineDto.getArticleId())))
                    .map(article -> {
                        orderLineList.add(new OrderLine(article, orderLineDto.getRequiredAmount()));
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(articleReact);
        }
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException(ORDER_ID_NOT_FOUND + orderId)))
                .map(orderData -> {
                    orderData.setDescription(orderDto.getDescription());
                    orderData.setOrderLines(orderLineList.toArray(new OrderLine[orderLineList.size()]));
                    return orderData;
                });
        return Mono.when(articlesFlux).then(order).then(this.orderReactRepository.saveAll(order).next()).map(OrderDto::new);
    }

    public Mono<OrderDto> getOrder(String orderId) {
        return this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException(ORDER_ID_NOT_FOUND + orderId))).map(OrderDto::new);
    }

    public Mono<OrderDto> closeOrder(String orderId, OrderDto orderDto) {
        List<OrderLine> orderLineList = new ArrayList<>();
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineDto.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException(ARTICLE_NOT_FOUND + orderLineDto.getArticleId())))
                    .map(article -> {
                        OrderLine orderLine = new OrderLine(article, orderLineDto.getRequiredAmount());
                        orderLine.setFinalAmount(orderLineDto.getFinalAmount());
                        orderLineList.add(orderLine);
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(articleReact);
        }
        Mono<Order> order = this.orderReactRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new NotFoundException(ORDER_ID_NOT_FOUND + orderId)))
                .map(orderToClose -> {
                    orderToClose.setOrderLines(orderLineList.toArray(new OrderLine[orderLineList.size()]));
                    orderToClose.close();
                    return orderToClose;
                });

        return Mono.when(articlesFlux).then(order)
                .then(this.updateArticlesStockAssured(orderDto))
                .then(this.orderReactRepository.saveAll(order).next())
                .map(OrderDto::new);
    }

    private Mono<Void> updateArticlesStockAssured(OrderDto orderDto) {
        Flux<Article> articlesFlux = Flux.empty();
        for (OrderLineDto orderLineDto : orderDto.getOrderLines()) {
            Mono<Article> articleReact = this.articleReactRepository.findById(orderLineDto.getArticleId())
                    .switchIfEmpty(Mono.error(new NotFoundException(ARTICLE_NOT_FOUND + orderLineDto.getArticleId())))
                    .map(article -> {
                        article.setStock(article.getStock() + orderLineDto.getFinalAmount());
                        return article;
                    });
            articlesFlux = articlesFlux.mergeWith(this.articleReactRepository.saveAll(articleReact));
        }
        return articlesFlux.then();
    }
}
