package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Status.Status;

import java.util.concurrent.ConcurrentHashMap;

public interface Adapter {
    void loadObject(PersistenceObject o);
    void saveObject(PersistenceObject o);
    void loadEdits(PersistenceObject o);
    Status getStatus();
    void clear();
    ConcurrentHashMap<Error, Boolean> getErrorMap();
    ConcurrentHashMap<Exception, Boolean> getExceptionMap();
}
