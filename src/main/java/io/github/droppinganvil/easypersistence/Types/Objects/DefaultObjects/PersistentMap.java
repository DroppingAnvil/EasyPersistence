package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Types.Objects.MappedBuildable;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.LoadedObject;
import io.github.droppinganvil.easypersistence.Types.TypeAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

public class PersistentMap implements MappedBuildable {
    public Class<?> getObjectClass() {
        return java.util.HashMap.class;
    }

    public Map<String, String> getSaveData(Object o, Field field, TypeAdapter ta) {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) o).entrySet()) {
            Object save1 = ta.getSave(entry.getKey(), entry.getKey().getClass());
            Object save2 = ta.getSave(entry.getValue(), entry.getValue().getClass());
            if (!(save1 instanceof String) || !(save2 instanceof String) ) {
                return null;
            }
            map.put((String) save1,(String) save2);
        }
        return map;
    }

    public Object build(Map<String, String> map, Object o, Field field, TypeAdapter ta) {
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            if (!(pt.getActualTypeArguments() == null || pt.getActualTypeArguments().length == 0)) {
                Class<?> type1 = (Class<?>) pt.getActualTypeArguments()[0];
                Class<?> type2 = (Class<?>) pt.getActualTypeArguments()[1];
                Map<Object, Object> builtMap = new HashMap<Object, Object>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    Object key = ta.getLoadedObject(entry.getKey(), type1);
                    Object value = ta.getLoadedObject(entry.getValue(), type2);
                    Error err = null;
                    //Needs cleanup
                    if (key instanceof Error) { err = (Error) key; }
                    if (value instanceof Error) { err = (Error) value; }
                    if (err != null) {
                        err.send();
                        return null;
                    }
                    builtMap.put(((LoadedObject) key).getObject(), ((LoadedObject) value).getObject());
                }
                return builtMap;
            }
        }
        return null;
    }
}
