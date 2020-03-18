package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.OrderController;
import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.dtos.OrderCreationDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderSearchDto;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDateTime;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(OrderResource.ORDERS)
public class OrderResource {

    public static final String ORDERS = "/orders";

    private OrderController orderController;

    @Autowired
    public OrderResource(OrderController orderController) {
        this.orderController = orderController;
    }

    @GetMapping
    public Flux<Order> search(@RequestParam (required = false) String description,
                              @RequestParam (required = false) String provider,
                              @RequestParam (required = false) String closingDate){
        OrderSearchDto orderSearchDto;

        if(closingDate.equals("null")){
            orderSearchDto = new OrderSearchDto(description, provider, null);
        } else {
            orderSearchDto = new OrderSearchDto(description, provider, LocalDateTime.now());
        }

        return this.orderController.searchOrder(orderSearchDto)
                .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PostMapping(produces = {"application/json"})
    public Mono<Order> createOrder(@Valid @RequestBody OrderCreationDto orderCreationDto){
        return this.orderController.createOrder(orderCreationDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
