package es.upm.miw.betca_tpv_spring.api_rest_controllers;

import es.upm.miw.betca_tpv_spring.business_controllers.CashierClosureController;
import es.upm.miw.betca_tpv_spring.dtos.*;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('OPERATOR')")
@RestController
@RequestMapping(CashierClosureResource.CASHIER_CLOSURES)
public class CashierClosureResource {

    public static final String CASHIER_CLOSURES = "/cashier-closures";
    public static final String LAST = "/last";
    public static final String STATE = "/state";
    public static final String DEPOSIT = "/deposit";
    public static final String WITHDRAWAL = "/withdrawal";
    public static final String CASHIER_CLOSURE_SEARCH = "/search";
    public static final String CASHIER_CLOSURE_SEARCH_BY_PARAMS = "/search-by-params";

    private CashierClosureController cashierClosureController;

    @Autowired
    public CashierClosureResource(CashierClosureController cashierClosureController) {
        this.cashierClosureController = cashierClosureController;
    }

    @PostMapping
    public Mono<Void> createCashierClosureOpened() {
        return cashierClosureController.createCashierClosureOpened()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = LAST)
    public Mono<CashierLastOutputDto> findCashierClosureLast() {
        return cashierClosureController.findCashierClosureLast()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = LAST + STATE)
    public Mono<CashierStateOutputDto> readStateFromLast() {
        return this.cashierClosureController.readTotalsFromLast()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PatchMapping(value = LAST)
    public Mono<Void> closeCashierClosure(@Valid @RequestBody CashierClosureInputDto cashierClosureInputDto) {
        return cashierClosureController.close(cashierClosureInputDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PatchMapping(value = LAST + DEPOSIT)
    public Mono<Void> cashierDeposit(@Valid @RequestBody CashMovementInputDto cashMovementInputDto) {
        return cashierClosureController.deposit(cashMovementInputDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @PatchMapping(value = LAST + WITHDRAWAL)
    public Mono<Void> cashierWithdrawal(@Valid @RequestBody CashMovementInputDto cashMovementInputDto) {
        return cashierClosureController.withdrawal(cashMovementInputDto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = CASHIER_CLOSURE_SEARCH)
    public Flux<CashierClosureSearchDto> readAll() {
        return this.cashierClosureController.readAll()
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

    @GetMapping(value = CASHIER_CLOSURE_SEARCH_BY_PARAMS)
    public Flux<CashierClosureSearchDto> search(@RequestParam(required = false) BigDecimal finalCash,
                                                @RequestParam(required = false) String closureDate,
                                                @RequestParam(required = false) String closureDateF) {
        LocalDateTime dateClosureDateFormat = null;
        LocalDateTime dateClosureDateFFormat = null;
        if (!closureDate.isEmpty() && !closureDateF.isEmpty()) {
            dateClosureDateFormat = LocalDateTime.parse(closureDate, DateTimeFormatter.ISO_DATE_TIME);
            dateClosureDateFormat = dateClosureDateFormat.plusDays(1);
            dateClosureDateFFormat = LocalDateTime.parse(closureDateF, DateTimeFormatter.ISO_DATE_TIME);
            dateClosureDateFFormat = dateClosureDateFFormat.plusDays(1);
        }
        CashierClosureSearchDto dto = new CashierClosureSearchDto();
        dto.setFinalCash(finalCash);
        dto.setClosureDate(dateClosureDateFormat);
        dto.setClosureDateF(dateClosureDateFFormat);

        return this.cashierClosureController.search(dto)
                .doOnNext(log -> LogManager.getLogger(this.getClass()).debug(log));
    }

}
