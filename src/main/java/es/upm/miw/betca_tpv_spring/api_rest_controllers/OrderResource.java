package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.OrderController;
import es.upm.miw.betca_tpv_spring.documents.Order;
import es.upm.miw.betca_tpv_spring.dtos.OrderCreationDto;
import es.upm.miw.betca_tpv_spring.dtos.OrderDto;
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

    public static final String ORDER_ID = "/{id}";

    public static final String ORDER_CLOSE = "/close";

    private OrderController orderController;

    @Autowired
    public OrderResource(OrderController orderController) {
        this.orderController = orderController;
    }

    @GetMapping
    public Flux<OrderDto> search(@RequestParam (required = false) String description,
                                 @RequestParam (required = false) String provider,
                                 @RequestParam (required = false) String closingDate){

        return this.orderController.searchOrder(new OrderSearchDto(description, provider, closingDate))
                .doOnEach(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PostMapping(produces = {"application/json"})
    public Mono<OrderDto> createOrder(@Valid @RequestBody OrderCreationDto orderCreationDto){
        return this.orderController.createOrder(orderCreationDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @DeleteMapping(value = ORDER_ID)
    public Mono<Void> deleteOrder(@PathVariable String id){
        return this.orderController.deleteOrder(id)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PutMapping(value = ORDER_ID, produces = {"application/json"})
    public Mono<OrderDto> updateOrder(@PathVariable String id, @Valid @RequestBody OrderDto orderDto){
        return this.orderController.updateOrder(id, orderDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = ORDER_ID)
    public Mono<OrderDto> getOrder(@PathVariable String id){
        return this.orderController.getOrder(id)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PutMapping(value = ORDER_CLOSE + ORDER_ID)
    public Mono<OrderDto> closeOrder(@PathVariable String id, @Valid @RequestBody OrderDto orderDto){
        return this.orderController.closeOrder(id, orderDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }
}
