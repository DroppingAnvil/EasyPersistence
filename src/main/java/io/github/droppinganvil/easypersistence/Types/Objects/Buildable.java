package io.github.droppinganvil.easypersistence.Types.Objects;
/**
 * For an object requiring multiple fields ComplexBuildable should be used in favor of Buildable
 * Please note this is only for generating objects for the registered class not the other way around
 * @since 1.0
 */
public interface Buildable {
    /**
     * This is used by the TypeAdapter to determine which Buildable to use it should be the class of your object that contains its constructor
     * @see io.github.droppinganvil.easypersistence.Types.TypeAdapter
     */
    Class<?> getObjectClass();

    /**
     * This is used to get the String that represents this objects data to later be loaded by build
     * @param o Your object
     */
    String getSaveData(Object o);

    /**
     * This is used to build your object based on the String you used to save it (getSaveData)
     * @param s String found
     * @return Your built object
     */
    Object build(String s);
}
