package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.betca_tpv_spring.documents.Staff;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StaffDto {
    private String id;
    private String mobile;
    private String year;
    private String month;
    private String day;
    private Integer workHours;
    private LocalDateTime lastLoginTime;


    public StaffDto() {
        // Empty for framework
    }

    public StaffDto(Staff staff) {
        this.id = staff.getId();
        this.mobile = staff.getMobile();
        this.year = staff.getYear();
        this.month = staff.getMonth();
        this.day = staff.getDay();
        this.workHours = staff.getWorkHours();
        this.lastLoginTime = staff.getLastLoginTime();
    }

    public StaffDto(String mobile, String year, String month, String day, Integer workHours, LocalDateTime lastLoginTime) {
        this.mobile = mobile;
        this.year = year;
        this.month = month;
        this.day = day;
        this.workHours = workHours;
        this.lastLoginTime = lastLoginTime;
    }

    public StaffDto(String mobile, String year, String month, String day) {
        this.year = year;
        this.mobile = mobile;
        this.month = month;
        this.day = day;
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
