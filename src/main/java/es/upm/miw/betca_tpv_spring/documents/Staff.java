package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Staff {
    @Id
    private String id;
    private String mobile;
    private String year;
    private String month;
    private String day;
    private Integer workHours;
    private LocalDateTime lastLoginTime;

    public Staff() {
    }

    public Staff(String id, String mobile, String year, String month, String day, Integer workHours, LocalDateTime lastLoginTime) {
        this.id = id;
        this.mobile = mobile;
        this.year = year;
        this.month = month;
        this.day = day;
        this.workHours = workHours;
        this.lastLoginTime = lastLoginTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getWorkHours() {
        return workHours;
    }

    public void setWorkHours(Integer workHours) {
        this.workHours = workHours;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && mobile.equals(((Staff) obj).mobile);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id='" + id + '\'' +
                ", mobile='" + mobile + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day='" + day + '\'' +
                ", workHours='" + workHours + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                '}';
    }

}
