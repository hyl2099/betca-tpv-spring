package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.OrderLine;
import es.upm.miw.betca_tpv_spring.documents.Provider;
import es.upm.miw.betca_tpv_spring.dtos.OrderCreationDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderSearchDto;
import es.upm.miw.betca_tpv_spring.repositories.ArticleRepository;
import es.upm.miw.betca_tpv_spring.repositories.OrderReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class OrderController {

    private OrderReactRepository orderReactRepository;

    private ProviderRepository providerRepository;

    private ArticleRepository articleRepository;

    @Autowired
    public OrderController(OrderReactRepository orderReactRepository, ProviderRepository providerRepository, ArticleRepository articleRepository) {
        this.orderReactRepository = orderReactRepository;
        this.providerRepository = providerRepository;
        this.articleRepository = articleRepository;
    }

    public Flux<Order> searchOrder(OrderSearchDto orderSearchDto) {
        Provider provider;

        if (orderSearchDto.getProviderId().equals("null")){
            provider = null;
        } else {
            provider = this.providerRepository.findById(orderSearchDto.getProviderId()).get();
        }

        if (orderSearchDto.getClosingDate() == null) {
            return this.orderReactRepository.findByDescriptionLikeOrProviderAndClosingDateIsNull(orderSearchDto.getDescription(), provider);
        } else {
            return this.orderReactRepository.findByDescriptionLikeOrProvider(orderSearchDto.getDescription(), provider);
        }
    }

    public Mono<Order> createOrder(OrderCreationDto orderCreationDto){
        Provider provider;
        if (orderCreationDto.getProviderId() == null) {
            provider = null;
        } else {
            provider = this.providerRepository.findById(orderCreationDto.getProviderId()).get();
        }
        OrderLine[] orderLines = new OrderLine[orderCreationDto.getOrderLines().length];
        String articleId;
        for (int i = 0; i < orderCreationDto.getOrderLines().length; i++){
            articleId = orderCreationDto.getOrderLines()[i].getArticleId();
            orderLines[i] = new OrderLine(this.articleRepository.findById(articleId).get(), orderCreationDto.getOrderLines()[i].getRequiredAmount());
        }

        return this.orderReactRepository.save(new Order(orderCreationDto.getDescription(), provider, orderLines));
    }
}
