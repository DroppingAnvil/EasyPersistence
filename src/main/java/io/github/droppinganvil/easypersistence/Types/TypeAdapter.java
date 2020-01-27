package io.github.droppinganvil.easypersistence.Types;


import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;
import io.github.droppinganvil.easypersistence.Types.Objects.ComplexBuildable;
import io.github.droppinganvil.easypersistence.Types.Objects.ObjectTypes;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.LoadedObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.Precision;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.Response;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.SaveData;

import java.util.Collection;
import java.util.HashMap;

public class TypeAdapter {
    private Class<?> extender;

    public TypeAdapter(Class<?> extender) {
        this.extender = extender;


    }

    private Boolean canCast(Class<?> c, Object o) {
        try {
            c.cast(o);
            new Info(InfoType.Cast_Success, Level.Processing).addMessage("Cast " + o.getClass().getName() + " to " + c.getName())
                    .complete().send();
            return true;
        } catch (ClassCastException e) {
            new Info(InfoType.Cast_Fail, Level.Processing).addMessage(e.getMessage()).complete().send();
            return false;
        }
    }
    public Response locate(Object o, HashMap<Class<?>, ?> map) {
        if (map.containsKey(o.getClass())) return new Response(Precision.Exact, map.get(o.getClass()).getClass());
        for (Class<?> c : map.keySet()) {
            if (canCast(c, o)) {
                return new Response(Precision.Cast, c);
            }
        }
        return new Response(Precision.None, null);
    }

    public Boolean load(Object targetMain, Object target, String fieldName) {
        try {
            loadSimple(fieldName, targetMain, getLoadedObject(target));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            new Error(ErrorType.File_Unknown).addMessage(e.getMessage()).complete().send();
            return false;
        }
        return true;
    }

    public SaveData getSaveData(Object o) {
        Boolean complex = false;
        Response response = locate(o, ObjectTypes.buildables);
        if (response.getPrecision() == Precision.None) {
            response = locate(o, ObjectTypes.complexBuildables);
            complex = true;
        }
        //If its still not found there is no chance of finding it and that object's class' author should be lectured immediately
        if (response.getPrecision() == Precision.None) {
            new Error(ErrorType.Non_Buildable_Object).addObject(o).complete().send();
            return null;
        }
        if (complex) {
            return new SaveData(ObjectTypes.complexBuildables.get(response.getTargetClass()).getSaveData(o));
        }
        return new SaveData(ObjectTypes.buildables.get(response.getTargetClass()).getSaveData(o));
    }
    //Give collection or string from disk as o
    public LoadedObject getLoadedObject(Object o) {
        Response response;
        Boolean complex = false;
        if (o instanceof Collection) {
            response = locate(o, ObjectTypes.complexBuildables);
            complex = true;
        } else {
            response = locate(o, ObjectTypes.buildables);
        }
        if (response.getPrecision() == Precision.None) {
            new Error(ErrorType.Non_Buildable_Object).addObject(o).complete().send();
            return null;
        }
        if (complex) {
            return new LoadedObject(
                    ObjectTypes.complexBuildables.get(response.getTargetClass()).build((Collection<String>) o),
                    ObjectTypes.complexBuildables.get(response.getTargetClass())
            );
        }
        return new LoadedObject(
                ObjectTypes.buildables.get(response.getTargetClass()).build((String) o),
                ObjectTypes.buildables.get(response.getTargetClass())
        );

    }

    private void loadSimple(String fieldName, Object o, LoadedObject loadedObject) throws NoSuchFieldException, IllegalAccessException {
        o.getClass().getField(fieldName).set(o, loadedObject.getObject());
    }

}
