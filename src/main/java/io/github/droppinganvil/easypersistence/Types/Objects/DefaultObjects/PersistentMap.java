package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Types.Objects.ComplexBuildable;
import io.github.droppinganvil.easypersistence.Types.TypeAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class PersistentMap implements ComplexBuildable {
    public Class<?> getObjectClass() {
        return java.util.HashMap.class;
    }

    public Collection<String> getSaveData(Object o, Field field, TypeAdapter ta) {
        Collection<String> coll = new ArrayList<String>();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) o).entrySet()) {
            Object save1 = ta.getSave(entry.getKey(), entry.getKey().getClass());
            Object save2 = ta.getSave(entry.getValue(), entry.getValue().getClass());
            if (!(save1 instanceof String) || !(save2 instanceof String) ) {
                return null;
            }
            //TODO Change to return Map<String, String> once ready
            coll.add(save1 + "@" + save2);
        }
        return coll;
    }

    public Object build(Collection<String> collection, Object o, Field field, TypeAdapter ta) {
        return null;
    }
}
