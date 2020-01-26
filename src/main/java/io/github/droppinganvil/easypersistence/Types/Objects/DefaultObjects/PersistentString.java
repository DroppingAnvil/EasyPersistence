package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;

/**
 * Java String processing
 * @see Buildable
 * @since 1.0
 */
public class PersistentString implements Buildable {
    public Class<?> getObjectClass() {
        return java.lang.String.class;
    }

    public String getSaveData(Object o) {
        return (String) o;
    }

    public Object build(String s) {
        return s;
    }
}
