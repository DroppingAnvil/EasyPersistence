package io.github.droppinganvil.easypersistence.Types.Objects.Response;

public class Response {
    private Precision precision;
    private Class<?> target;
    private Type type;


    public Response(Precision precision, Class<?> target, Type type) {
        this.precision = precision;
        this.target = target;
        this.type = type;
    }

    public Class<?> getTargetClass() {
        return target;
    }

    public Precision getPrecision() {
        return precision;
    }

    public Type getType() {return type;}
}
