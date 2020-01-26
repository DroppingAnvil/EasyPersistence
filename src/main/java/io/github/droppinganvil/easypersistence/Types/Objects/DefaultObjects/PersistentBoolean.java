package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;

/**
 * Java Boolean processing
 * @since 1.0
 */
public class PersistentBoolean implements Buildable {
    public Class<?> getObjectClass() {
        return java.lang.Boolean.class;
    }

    public String getSaveData(Object o) {
        return o.toString();
    }

    public Object build(String s) {
        return Boolean.parseBoolean(s);
    }
}
