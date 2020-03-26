package es.upm.miw.betca_tpv_spring.business_controllers;

import es.upm.miw.betca_tpv_spring.TestConfig;
import es.upm.miw.betca_tpv_spring.documents.AlarmArticle;
import es.upm.miw.betca_tpv_spring.documents.StockAlarm;
import es.upm.miw.betca_tpv_spring.dtos.StockAlarmOutputDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@TestConfig
class StockAlarmControllerIT {

    @Autowired
    private StockAlarmController stockAlarmController;
    private StockAlarmOutputDto stockAlarmOutputDto;

    @BeforeEach
    void seed() {
        AlarmArticle[] alarmArticle ={
                new AlarmArticle("1",500,1500),
                new AlarmArticle("8400000000017",15,20)
        };

        this.stockAlarmOutputDto = new StockAlarmOutputDto(
                new StockAlarm("222","2222","upm",2,2, alarmArticle)
        );
    }

    @Test
    void testStockAlarmSearchWarning(){
        String warning = "warning";
        StepVerifier
                .create(this.stockAlarmController.searchWarning(warning))
                .expectNextMatches(stockAlarmOutputDto ->{
                    assertEquals("222",stockAlarmOutputDto.getId());
                    assertEquals(1,stockAlarmOutputDto.getAlarmArticle().length);
                    return true;
                })
                .expectComplete()
                .verify();
    }

    @Test
    void testStockAlarmSearchCritical(){
        String critical = "critical";
        StepVerifier
                .create(this.stockAlarmController.searchWarning(critical))
                .expectNextMatches(stockAlarmOutputDto ->{
                    assertEquals("222",stockAlarmOutputDto.getId());
                    assertEquals(2,stockAlarmOutputDto.getAlarmArticle().length);
                    return true;
                })
                .expectComplete()
                .verify();
    }

}

