package io.github.droppinganvil.easypersistence.Types.Objects;

import java.util.HashMap;
/**
 * Add a Buildable or ComplexBuildable to their respective maps for your object to be recognized
 * @since 1.0
 */
public class ObjectTypes {
    public static HashMap<Class<?>, Buildable> buildables = new HashMap<Class<?>, Buildable>();
    public static HashMap<Class<?>, ComplexBuildable> complexBuildables = new HashMap<Class<?>, ComplexBuildable>();
    public static HashMap<Class<?>, MappedBuildable> mappedBuildables = new HashMap<Class<?>, MappedBuildable>();
}
