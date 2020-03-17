package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.documents.Provider;
import es.upm.miw.betca_tpv_spring.dtos.OrderSearchDto;
import es.upm.miw.betca_tpv_spring.repositories.OrderReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.ProviderReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.ProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class OrderController {

    private OrderReactRepository orderReactRepository;

    private ProviderRepository providerRepository;

    @Autowired
    public OrderController(OrderReactRepository orderReactRepository, ProviderRepository providerRepository) {
        this.orderReactRepository = orderReactRepository;
        this.providerRepository = providerRepository;
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
}
