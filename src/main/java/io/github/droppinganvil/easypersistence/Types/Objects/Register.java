package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.PersistentBoolean;
import io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.PersistentDouble;
import io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.PersistentInteger;
import io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.PersistentString;

/**
 * Registers all default object handling
 */
public class Register {
    public static void defaults() {
        register(new PersistentString());
        register(new PersistentInteger());
        register(new PersistentBoolean());
        register(new PersistentDouble());
    }
    public static void register(Buildable b) {
        ObjectTypes.buildables.put(b.getObjectClass(), b);
    }
}
