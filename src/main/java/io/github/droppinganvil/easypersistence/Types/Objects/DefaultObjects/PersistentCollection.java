package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.ComplexBuildable;

import java.util.Collection;

public class PersistentCollection implements ComplexBuildable {
    @Override
    public Class<?> getObjectClass() {
        return java.util.Collection.class;
    }

    @Override
    public Collection<String> getSaveData(Object o) {
        return null;
    }

    @Override
    public Object build(Collection<String> collection) {
        return null;
    }
}
