package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.Types.TypeAdapter;

import java.lang.reflect.Field;
import java.util.Collection;
/**
 * For an object requiring multiple fields ComplexBuildable should be used in favor of Buildable
 * Please note you cannot build an Error it will be detected as an actual error and reported!
 * For an example on how to implement Buildable
 * @see io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.PersistentInteger
 * For an example on how to implement ComplexBuildable
 * @see io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects.PersistentCollection
 * @since 1.0
 */
public interface ComplexBuildable {

    Class<?> getObjectClass();
    //TODO Accept Map<String, String>
    Collection<String> getSaveData(Object o, Field field, TypeAdapter ta);

    /**
     * When called this should return a new object of its type with given parameters
     * @param collection Collection found on disk
     * @param o Default object found on the PersistenceObject
     * @param field Field that o was retrieved from. Useful for determining an element type
     * @param ta TypeAdapter instance. Useful for getting the builders for different buildable objects
     * @return Built Object
     */
    Object build(Collection<String> collection, Object o, Field field, TypeAdapter ta);

}
