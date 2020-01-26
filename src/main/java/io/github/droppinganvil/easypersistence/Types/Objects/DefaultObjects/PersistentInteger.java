package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;

/**
 * Java Integer processing
 * @see Buildable
 * @since 1.0
 */
public class PersistentInteger implements Buildable {
    public Class<?> getObjectClass() {
        return java.lang.Integer.class;
    }

    public String getSaveData(Object o) {
        return String.valueOf(o);
    }

    public Object build(String s) {
        return Integer.parseInt(s);
    }
}
