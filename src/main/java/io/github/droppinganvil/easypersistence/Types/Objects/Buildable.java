package io.github.droppinganvil.easypersistence.Types.Objects;
/**
 * For an object requiring multiple fields ComplexBuildable should be used in favor of Buildable
 * Please note this is only for generating objects for the registered class not the other way around
 * @since 1.0
 */
public interface Buildable {

    Class<?> getObjectClass();
    String getSaveData(Object o);
    Object build(String s);
}
