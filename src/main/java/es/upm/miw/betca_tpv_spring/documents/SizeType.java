package es.upm.miw.betca_tpv_spring.documents;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SizeType {

    @Id
    private String id;
    private String name;

    public SizeType(){

    }

    public  SizeType(String id, String description)
    {
        this.id  = id;
        this.name = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj != null && getClass() == obj.getClass() && (id.equals(((SizeType) obj).id));
    }

    @Override
    public String toString() {
        return "SizeType{" +
                "id='" + id + '\'' +
                ", name='" + name +
                '}';
    }

}