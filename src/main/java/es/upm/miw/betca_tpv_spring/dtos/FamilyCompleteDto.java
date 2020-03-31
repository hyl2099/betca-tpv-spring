package es.upm.miw.betca_tpv_spring.dtos;


public class FamilyCompleteDto {
    private String description;
    private String provider;
    private String sizeType;
    private String fromSize;
    private String toSize;
    private String reference;
    private int increment;

    public FamilyCompleteDto(FamilyCompleteDto articlesFamilyComplete) {
        this.description = articlesFamilyComplete.getDescription();
        this.sizeType = articlesFamilyComplete.getSizeType();
        this.provider = articlesFamilyComplete.getProvider();
        this.fromSize = articlesFamilyComplete.getFromSize();
        this.toSize = articlesFamilyComplete.getToSize();
        this.increment = articlesFamilyComplete.getIncrement();
        this.reference = articlesFamilyComplete.getRefence();
    }

    public FamilyCompleteDto() {

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

    public String getSizeType() {
        return sizeType;
    }

    public void setSizeType(String sizeType) {
        this.sizeType = sizeType;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public String getFromSize() {
        return fromSize;
    }

    public void setFromSize(String fromSize) {
        this.fromSize = fromSize;
    }

    public String getToSize() {
        return toSize;
    }

    public void setToSize(String toSize) {
        this.toSize = toSize;
    }

    public String getRefence() {
        return reference;
    }

    public void setRefence(String reference) {
        this.reference = reference;
    }
}
