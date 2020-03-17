package es.upm.miw.betca_tpv_spring.documents;

public enum Tax {
    FREE(0), SUPER_REDUCED(4), REDUCED(10), GENERAL(21);

    private final int tax;

    Tax(int tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        return this.name() + "(" + this.tax + ")";
    }
}
