package io.github.droppinganvil.easypersistence.Types.Objects.Response;

public class Response {
    private Precision precision;
    private Class<?> target;
    private Boolean complex;

    public Response(Precision precision, Class<?> target, Boolean complex) {
        this.precision = precision;
        this.target = target;
        this.complex = complex;
    }

    public Class<?> getTargetClass() {
        return target;
    }

    public Precision getPrecision() {
        return precision;
    }

    public Boolean isComplex() {return complex;}
}
