package io.github.droppinganvil.easypersistence.Types.Objects.Response;

public enum Precision {
    Exact(2),
    Cast(1),
    None(0)
    ;

    private int i;

    Precision(int i) {
        this.i = i;
    }
    public int i() {
        return i;
    }
}
