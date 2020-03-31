package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.documents.CustomerDiscount;
import es.upm.miw.betca_tpv_spring.dtos.CustomerDiscountDto;
import es.upm.miw.betca_tpv_spring.exceptions.BadRequestException;
import es.upm.miw.betca_tpv_spring.exceptions.NotFoundException;
import es.upm.miw.betca_tpv_spring.repositories.CustomerDiscountReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.CustomerDiscountRepository;
import es.upm.miw.betca_tpv_spring.repositories.UserReactRepository;
import es.upm.miw.betca_tpv_spring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class CustomerDiscountController {

    private static final String USER_NOT_FOUND = "User not found";
    private CustomerDiscountRepository customerDiscountRepository;
    private CustomerDiscountReactRepository customerDiscountReactRepository;
    private UserRepository userRepository;
    private UserReactRepository userReactRepository;

    @Autowired
    CustomerDiscountController(CustomerDiscountReactRepository customerDiscountReactRepository, UserReactRepository userReactRepository,
                               CustomerDiscountRepository customerDiscountRepository, UserRepository userRepository) {
        this.customerDiscountReactRepository = customerDiscountReactRepository;
        this.customerDiscountRepository = customerDiscountRepository;
        this.userReactRepository = userReactRepository;
        this.userRepository = userRepository;
    }

    public Mono<CustomerDiscountDto> findByUserMobile(String mobile) {
        return this.customerDiscountReactRepository.findByUser(userReactRepository.findByMobile(mobile))
                .switchIfEmpty(Mono.empty())
                .map(CustomerDiscountDto::new);
    }

    public Mono<CustomerDiscountDto> createCustomerDiscount(CustomerDiscountDto customerDiscountDto) {
        CustomerDiscount customerDiscount = new CustomerDiscount();
        if (customerDiscountDto.getUser().getMobile() == null) {
            userReactRepository = Mono.error(new BadRequestException("User can't be null"));
        } else {
            userReactRepository = this.userReactRepository(customerDiscountDto.getUserByMobile())
                    .switchIfEmpty(Mono.error(new NotFoundException(USER_NOT_FOUND + customerDiscountDto.getUserByMobile())))
                    .doOnNext(customerDiscount::getUser).then;
        }
        return Mono
                .when(customerDiscount)
                .then(this.customerDiscountReactRepository.save(customerDiscount)).map(CustomerDiscountDto::new);

    }

    public Mono<CustomerDiscountDto> updateCustomerDiscount(String customerDiscountId, CustomerDiscountDto customerDiscountDto) {
        Mono<CustomerDiscount> customerDiscountMono = this.customerDiscountReactRepository.findById(customerDiscountId)
                .switchIfEmpty(Mono.error(new NotFoundException("CustomerDiscount Id " + customerDiscountId)))
                .map(customerDiscount1 -> {
                    customerDiscount1.setDescription(customerDiscountDto.getDescription());
                    customerDiscount1.setDiscount(customerDiscountDto.getDiscount());
                    customerDiscount1.setMinimumPurchase(customerDiscountDto.getMinimumPurchase());
                    customerDiscount1.getUser();
                    return customerDiscount1;
                });
        return Mono
                .when(customerDiscountMono)
                .then(this.customerDiscountReactRepository.saveAll(customerDiscountMono).next().map(CustomerDiscountDto::new));

    }

    public Mono<Void> deleteCustomerDiscount(String customerDiscountId) {
        Mono<CustomerDiscount> customerDiscountMono = this.customerDiscountReactRepository.findById(customerDiscountId);
        return Mono
                .when(customerDiscountMono)
                .then(this.customerDiscountReactRepository.deleteById(customerDiscountId));
    }


}