package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.CustomerPoints;
import es.upm.miw.betca_tpv_spring.documents.User;
import es.upm.miw.betca_tpv_spring.dtos.CustomerPointsDto;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.CustomerPointsReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.UserReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;


@Controller
public class CustomerPointsController {

    private CustomerPointsReactRepository customerPointsReactRepository;
    private UserReactRepository userReactRepository;

    @Autowired
    public CustomerPointsController(CustomerPointsReactRepository customerPointsReactRepository, UserReactRepository userReactRepository) {
        this.customerPointsReactRepository = customerPointsReactRepository;
        this.userReactRepository = userReactRepository;
    }

    public Mono<Void> consumeAllCustomerPointsByUserMobile(String mobile) {

        Mono<User> user = this.userReactRepository.findByMobile(mobile);

        Mono<CustomerPoints> customerPointsMono = this.customerPointsReactRepository.findByUser(user)
                .map(doc -> {
                    doc.setPoints(0);
                    System.out.println("HOLAA2" + doc.toString());
                    return doc;
                });

        return user.then(this.customerPointsReactRepository.saveAll(customerPointsMono).then());
    }
}
