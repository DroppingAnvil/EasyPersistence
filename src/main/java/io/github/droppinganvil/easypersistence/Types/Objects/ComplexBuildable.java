package io.github.droppinganvil.easypersistence.Types.Objects;

import java.util.Collection;
/**
 * For an object requiring multiple fields ComplexBuildable should be used in favor of Buildable
 * For an example on how
 * @since 1.0
 */
public interface ComplexBuildable {

    Class<?> getObjectClass();
    Collection<String> getSaveData(Object o);
    Object build(Collection<String> collection);

}
