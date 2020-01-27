package io.github.droppinganvil.easypersistence.Types.Objects.Response;

public class Response {
    private Precision precision;
    private Class<?> target;

    public Response(Precision precision, Class<?> target) {
        this.precision = precision;
        this.target = target;
    }

    public Class<?> getTargetClass() {
        return target;
    }

    public Precision getPrecision() {
        return precision;
    }
}
