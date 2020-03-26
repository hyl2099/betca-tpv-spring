package es.upm.miw.betca_tpv_spring.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Size {

    @Id
    private String id;
    private String description;
    @DBRef
    private SizeType sizeType;

    public Size(){

    }

    public  Size(String id, String description, SizeType sizeType)
    {
        this.id  = id;
        this.description = description;
        this.sizeType = sizeType;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public SizeType getSizeType() {
        return sizeType;
    }

    @Override
    public String toString() {
        return "Size{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", sizeType='" + sizeType +
                '}';
    }

}