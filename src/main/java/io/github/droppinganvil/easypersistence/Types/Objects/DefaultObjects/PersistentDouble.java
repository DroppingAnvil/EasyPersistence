package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;

public class PersistentDouble implements Buildable {
    public Class<?> getObjectClass() {
        return java.lang.Double.class;
    }

    public String getSaveData(Object o) {
        return o.toString();
    }

    public Object build(String s) {
        return Double.parseDouble(s);
    }
}
