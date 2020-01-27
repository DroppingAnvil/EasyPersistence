package io.github.droppinganvil.easypersistence.Types;


import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;
import io.github.droppinganvil.easypersistence.Types.Objects.ObjectTypes;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.Precision;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.Response;

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

    public void directProcessing(String fieldName, String fieldValue, Object o, Object oo, Boolean load) {
        Response response = locate(o, ObjectTypes.buildables);
        if (response.getPrecision() == Precision.None) {
            response = locate(o, ObjectTypes.complexBuildables);
        }
        //If its still not found there is no chance of finding it and that object's class' author should be lectured immediately
        if (response.getPrecision() == Precision.None) {
            new Error(ErrorType.Non_Buildable_Object).addObject(o).complete().send();
            return;
        }
        try {
            if (load) {
                loadSimple(fieldName, fieldValue, o, oo, response);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            //TODO Use Notifications.ErrorHandling.Error after debugging
            e.printStackTrace();
        }
    }

    private void processCollection(Object o) {

    }

    private void loadSimple(String fieldName, String fieldValue, Object o, Object oo, Response r) throws NoSuchFieldException, IllegalAccessException {
        o.getClass().getField(fieldName).set(oo, Buildable.class.cast(r.getTargetClass()).build(fieldValue));
    }

}
