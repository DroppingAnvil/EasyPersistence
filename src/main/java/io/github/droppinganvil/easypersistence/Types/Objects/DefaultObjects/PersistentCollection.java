package io.github.droppinganvil.easypersistence.Types.Objects.DefaultObjects;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Types.Objects.ComplexBuildable;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.LoadedObject;
import io.github.droppinganvil.easypersistence.Types.TypeAdapter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;

public class PersistentCollection implements ComplexBuildable {
    public Class<?> getObjectClass() {
        return java.util.Collection.class;
    }

    public Collection<String> getSaveData(Object o, Field field, TypeAdapter ta) {
                Collection<String> coll = new ArrayList<String>();
                for (Object obj : (Collection<?>) o) {
                    Object save = ta.getSave(obj, obj.getClass());
                    if (!(save instanceof String)) {
                        // Returning null is safe here as it should get handled in the Adapter.
                        return null;
                    }
                    coll.add((String) obj);
                }
                return coll;
    }

    public Object build(Collection<String> collection, Object o, Field field, TypeAdapter ta) {
        if (field.getGenericType() instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) field.getGenericType();
            if (!(pt.getActualTypeArguments() == null || pt.getActualTypeArguments().length == 0)) {
                Class<?> sh = (Class<?>) pt.getActualTypeArguments()[0];
                Collection<Object> coll = new ArrayList<Object>();
                for (String value : collection) {
                    Object built = ta.getLoadedObject(value, sh);
                    if (built instanceof Error) {
                        Error e = (Error) built;
                        e.send();
                        return null;
                    }
                    coll.add(((LoadedObject) built).getObject());
                }
                return coll;
            }
        }
        throwRawError(field, o);
            return null;
    }

    private void throwRawError(Field field, Object o) {
        Error error = new Error(ErrorType.Raw_Usage).addMessage("Collection '" + field.getName() + "' in '" + o.getClass().getName() + "' must have arguments!")
                .addObject(o).complete();
        error.send();
    }
}
