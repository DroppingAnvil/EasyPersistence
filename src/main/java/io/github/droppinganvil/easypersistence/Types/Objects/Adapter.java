package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Status.Status;

public interface Adapter {
    void loadObject(PersistenceObject o);
    void saveObject(PersistenceObject o);
    Status getStatus();
    void clear();
}
