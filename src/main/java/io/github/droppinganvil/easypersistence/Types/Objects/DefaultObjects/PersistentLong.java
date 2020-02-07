package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;

public class PersistentLong implements Buildable {
    public Class<?> getObjectClass() {
        return java.lang.Long.class;
    }

    public String getSaveData(Object o) {
        return ((Long) o).toString();
    }

    public Object build(String s) {
        return Long.parseLong(s);
    }
}
