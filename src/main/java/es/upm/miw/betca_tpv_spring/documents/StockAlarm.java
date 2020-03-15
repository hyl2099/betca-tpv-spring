package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;

@Document
public class StockAlarm {

    @Id
    private String id;
    private String description;
    private String provider;
    private Integer warning;
    private Integer critical;
    private AlarmArticle[] alarmArticle;

    public StockAlarm() {
    }

    public StockAlarm(String id, String description, String provider, Integer warning, Integer critical, AlarmArticle[] alarmArticle) {
        this.id = id;
        this.description = description;
        this.provider = provider;
        this.warning = warning;
        this.critical = critical;
        this.alarmArticle = alarmArticle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
        return "StockAlarm{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", provider='" + provider + '\'' +
                ", warning=" + warning +
                ", critical=" + critical +
                ", alarmArticle=" + Arrays.toString(alarmArticle) +
                '}';
    }
}
