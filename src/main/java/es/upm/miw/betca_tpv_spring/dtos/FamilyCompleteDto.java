package es.upm.miw.betca_tpv_spring.dtos;


public class FamilyCompleteDto {
    private String description;
    private String provider;
    private boolean sizeType;
    private String fromSize;
    private String toSize;
    private String reference;
    private int increment;

    public static Builder builder() {
        return new Builder();
    }

    public FamilyCompleteDto(FamilyCompleteDto articlesFamilyComplete) {
        this.description = articlesFamilyComplete.getDescription();
        this.sizeType = articlesFamilyComplete.getSizeType();
        this.provider = articlesFamilyComplete.getProvider();
        this.fromSize = articlesFamilyComplete.getFromSize();
        this.toSize = articlesFamilyComplete.getToSize();
        this.increment = articlesFamilyComplete.getIncrement();
        this.reference = articlesFamilyComplete.getReference();
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

    public boolean getSizeType() {
        return sizeType;
    }

    public void setSizeType(boolean sizeType) {
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }


    public static class Builder {
        private FamilyCompleteDto familyCompleteDto;

        private Builder() {
            this.familyCompleteDto = new FamilyCompleteDto();
            this.familyCompleteDto.reference = "";
            this.familyCompleteDto.description = "";
            this.familyCompleteDto.provider = "";
            this.familyCompleteDto.fromSize = "";
            this.familyCompleteDto.toSize = "";
            this.familyCompleteDto.sizeType = false;
            this.familyCompleteDto.increment = 0;
        }

        public Builder reference(String reference) {
            this.familyCompleteDto.reference = reference;
            return this;
        }

        public Builder description(String description) {
            this.familyCompleteDto.description = description;
            return this;
        }

        public Builder provider(String provider) {
            this.familyCompleteDto.provider = provider;
            return this;
        }

        public Builder fromSize(String fromSize) {
            this.familyCompleteDto.fromSize = fromSize;
            return this;
        }


        public Builder toSize(String toSize) {
            this.familyCompleteDto.toSize = toSize;
            return this;
        }

        public Builder sizeType(boolean sizeType) {
            this.familyCompleteDto.sizeType = sizeType;
            return this;
        }

        public Builder increment(int increment) {
            this.familyCompleteDto.increment = increment;
            return this;
        }

        public FamilyCompleteDto build() {
            return this.familyCompleteDto;
        }
    }
}
