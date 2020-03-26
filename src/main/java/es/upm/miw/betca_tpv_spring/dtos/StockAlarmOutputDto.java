package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.AlarmArticle;
import es.upm.miw.betca_tpv_spring.documents.StockAlarm;

import java.util.Arrays;

public class StockAlarmOutputDto extends StockAlarmInputDto {

    private String id;

    public StockAlarmOutputDto() {
    }

    public StockAlarmOutputDto(String id, String description, String provider, Integer warning, Integer critical, AlarmArticle[] alarmArticle) {
        super(description, provider, warning, critical, alarmArticle);
        this.id = id;
    }

    public StockAlarmOutputDto(StockAlarm stockAlarm) {
        this(stockAlarm.getId(), stockAlarm.getDescription(), stockAlarm.getProvider(), stockAlarm.getWarning(), stockAlarm.getCritical(), stockAlarm.getAlarmArticle());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "StockAlarmOutputDto{" +
                "id='" + id + '\'' +
                ", description='" + super.getDescription() + '\'' +
                ", provider='" + super.getProvider() + '\'' +
                ", warning=" + super.getWarning() +
                ", critical=" + super.getCritical() +
                ", alarmArticle=" + Arrays.toString(super.getAlarmArticle()) +
                '}';
    }
}
