package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.*;

/**
 * Registers all default object handling
 */
public class Register {
    public static void defaults() {
        register(new PersistentString());
        register(new PersistentInteger());
        register(new PersistentBoolean());
        register(new PersistentDouble());
        register(new PersistentCollection());
        register(new PersistentLong());
    }
    public static void register(Buildable b) {
        ObjectTypes.buildables.put(b.getObjectClass(), b);
    }
    public static void register(ComplexBuildable cb) {ObjectTypes.complexBuildables.put(cb.getObjectClass(), cb);}
}
