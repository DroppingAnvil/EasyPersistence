package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.Types.TypeAdapter;

import java.lang.reflect.Field;
import java.util.Map;

public interface MappedBuildable {

    Class<?> getObjectClass();
    Map<String, String> getSaveData(Object o, Field field, TypeAdapter ta);

    /**
     * When called this should return a new object of its type with given parameters
     * @param map Map found on disk
     * @param o Default object found on the PersistenceObject
     * @param field Field that o was retrieved from. Useful for determining an element type
     * @param ta TypeAdapter instance. Useful for getting the builders for different buildable objects
     * @return Built Object
     */
    Object build(Map<String, String> map, Object o, Field field, TypeAdapter ta);

}
