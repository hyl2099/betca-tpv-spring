package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class CustomerPoints {

    @Id
    private String id;
    private int points;
    @DBRef(lazy = true)
    private User user;

    public CustomerPoints() {

    }

    public CustomerPoints(String id, int points, User user) {
        this.id = id;
        this.points = points;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public User getUser() {
        return user;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", user=" + user +
                '}';
    }
}
