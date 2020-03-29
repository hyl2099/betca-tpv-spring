package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.dtos.TicketOutputDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.List;

@ApiTestConfig
class TicketTrackingResourceIT {

    @Autowired
    private RestService restService;

    @Autowired
    private WebTestClient webTestClient;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Test
    void testGetTicketByReference() {
        List<TicketOutputDto> tickets = this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + TicketResource.TICKETS)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TicketOutputDto.class)
                .returnResult().getResponseBody();
        assert tickets != null;
        TicketOutputDto ticket = this.restService.loginAdmin(webTestClient)
                .get().uri(contextPath + TicketTrackingResource.TICKET_TRACKING + TicketTrackingResource.REFERENCE, tickets.get(0).getReference())
                .exchange()
                .expectStatus().isOk()
                .expectBody(TicketOutputDto.class)
                .returnResult().getResponseBody();
        assertNotNull(ticket);
        assertEquals("201901121", ticket.getId());
        assertNotNull(ticket.getShoppingList());
        assertNotNull(ticket.getShoppingList()[0]);
        assertEquals("8400000000017", ticket.getShoppingList()[0].getCode());
    }
}
