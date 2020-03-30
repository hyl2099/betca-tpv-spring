package es.upm.miw.betca_tpv_spring.dtos;


import es.upm.miw.betca_tpv_spring.documents.CustomerPoints;
import es.upm.miw.betca_tpv_spring.documents.User;

public class CustomerPointsDto {

    private String id;
    private Integer points;
    private User user;

    public CustomerPointsDto() {
        // Empty
    }

    public CustomerPointsDto(String id, Integer points, User user) {
        this();
        this.id = id;
        this.points = points;
        this.user = user;
    }

    public CustomerPointsDto(CustomerPoints customerPoints) {
        this(customerPoints.getId(), customerPoints.getPoints(), customerPoints.getUser());
    }

    public String getId() {
        return id;
    }

    public Integer getPoints() {
        return points;
    }

    public User getUser() {
        return user;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "CustomerPointsDto{" +
                "id='" + id + '\'' +
                ", points=" + points +
                ", user=" + user +
                '}';
    }
}
