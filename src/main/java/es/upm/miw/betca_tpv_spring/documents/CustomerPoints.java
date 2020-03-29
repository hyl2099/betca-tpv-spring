package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class CustomerPoints {

    @Id
    private String id;
    private Integer points;
    private LocalDateTime registrationDate;
    @DBRef(lazy = true)
    private User user;

    public CustomerPoints() {
        this.registrationDate = LocalDateTime.now();
    }

    public CustomerPoints(String id, Integer points, LocalDateTime registrationDate, User user) {
        this();
        this.id = id;
        this.points = points;
        this.registrationDate = registrationDate;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public Integer getPoints() {
        return points;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public User getUser() {
        return user;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && (id.equals(((CustomerPoints) obj).id));
    }

    @Override
    public String toString() {
        return "CustomerPoints{" +
                "id='" + id + '\'' +
                ", points=" + points +
                ", registrationDate=" + registrationDate +
                ", user=" + user +
                '}';
    }
}
