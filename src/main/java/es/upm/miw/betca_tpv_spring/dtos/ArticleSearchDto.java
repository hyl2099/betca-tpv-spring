package es.upm.miw.betca_tpv_spring.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ArticleSearchDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String provider;

    public ArticleSearchDto() {
        // Empty for framework
    }

    public ArticleSearchDto(String description, String provider) {
        this.description = description;
        this.provider = provider;
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

    @Override
    public String toString() {
        return "ArticleSearchDto{" +
                "description=" + this.description +
                ", provider=" + this.provider +
                 '\'' +
                '}';
    }
}
