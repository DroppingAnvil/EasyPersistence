package io.github.droppinganvil.easypersistence.Types.Objects;

public interface Adapter {
    void loadObject(Object o);
    void saveObject(Object o);
}
