package es.upm.miw.betca_tpv_spring.dtos;

public class SizeTypeDto {
    private String id;
    private String name;

    public SizeTypeDto(){

    }

    public  SizeTypeDto(String id, String name)
    {
        this.id  = id;
        this.name = name;
    }

    public  SizeTypeDto(SizeTypeDto sizeTypeDto)
    {
        this.id  = sizeTypeDto.getId();
        this.name = sizeTypeDto.getName();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SizeTypeDto{" +
                "id='" + id + '\'' +
                ", name='" + name +
                '}';
    }
}
