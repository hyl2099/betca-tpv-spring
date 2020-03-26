package es.upm.miw.betca_tpv_spring.dtos;

import es.upm.miw.betca_tpv_spring.documents.AlarmArticle;
import es.upm.miw.betca_tpv_spring.documents.StockAlarm;

import javax.validation.constraints.NotNull;
import java.util.Arrays;

public class StockAlarmInputDto {

    @NotNull
    private String description;
    private String provider;
    private Integer warning;
    private Integer critical;
    private AlarmArticle[] alarmArticle;

    public StockAlarmInputDto() {
    }

    public StockAlarmInputDto(@NotNull String description) {
        this.description = description;
    }

    public StockAlarmInputDto(@NotNull String description, String provider, Integer warning, Integer critical, AlarmArticle[] alarmArticle) {
        this.description = description;
        this.provider = provider;
        this.warning = warning;
        this.critical = critical;
        this.alarmArticle = alarmArticle;
    }

    public StockAlarmInputDto(StockAlarm stockAlarm) {
        this(stockAlarm.getDescription(), stockAlarm.getProvider(), stockAlarm.getWarning(), stockAlarm.getCritical(), stockAlarm.getAlarmArticle());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Integer getWarning() {
        return warning;
    }

    public void setWarning(Integer warning) {
        this.warning = warning;
    }

    public Integer getCritical() {
        return critical;
    }

    public void setCritical(Integer critical) {
        this.critical = critical;
    }

    public AlarmArticle[] getAlarmArticle() {
        return alarmArticle;
    }

    public void setAlarmArticle(AlarmArticle[] alarmArticle) {
        this.alarmArticle = alarmArticle;
    }

    @Override
    public String toString() {
        return "StockAlarmInputDto{" +
                "description='" + description + '\'' +
                ", provider='" + provider + '\'' +
                ", warning=" + warning +
                ", critical=" + critical +
                ", alarmArticle=" + Arrays.toString(alarmArticle) +
                '}';
    }
}
