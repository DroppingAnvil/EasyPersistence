package io.github.droppinganvil.easypersistence.Types;


import io.github.droppinganvil.easypersistence.Configuration.Config;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Adapter;
import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;
import io.github.droppinganvil.easypersistence.Types.Objects.ObjectTypes;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.LoadedObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.Precision;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.Response;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.SaveData;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TypeAdapter {

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
    public Response locate(Object o, HashMap<Class<?>, ?> map, Boolean complex) {
        if (map.containsKey(o.getClass())) return new Response(Precision.Exact, map.get(o.getClass()).getClass(), complex);
        for (Class<?> c : map.keySet()) {
            if (canCast(c, o)) {
                return new Response(Precision.Cast, c, complex);
            }
        }
        return new Response(Precision.None, null, complex);
    }

    public Error load(Object targetMain, Object target, Field field) throws IllegalAccessException {
        Object o = getLoadedObject(target, field);
        if (o instanceof Error) return (Error) o;
            setField(field, targetMain, (LoadedObject) o);
            return null;
    }

    public Object build(String s, Class<?> clazz) {
        if (ObjectTypes.buildables.containsKey(clazz)) {
            Buildable b = ObjectTypes.buildables.get(clazz);
            try {
                return ObjectTypes.buildables.get(clazz).build(s);
            } catch (Exception e) {
                e.printStackTrace();
                if (b != null) {
                    Error error = new Error(ErrorType.Issue_Generic)
                            .addMessage("Exception produced on using buildable '" + b.getClass().getName() + "' located in " + b.getClass().getPackage())
                            .addObject(b).complete();
                    error.send();
                    return error;
                }
                Error error = new Error(ErrorType.Issue_Generic).addMessage(e.getMessage()).complete();
                error.send();
                return error;
            }
        } else {
            return new Error(ErrorType.Non_Buildable_Object).addMessage("No buildable found for " + clazz.getName()).addObject(s).complete();
        }
    }

    /**
     * Simple retriever designed to work with Collections to generate elements
     * @param o Object
     * @param clazz Target Def
     * @return String or Error
     */
    public Object getSave(Object o, Class<?> clazz) {
        if (ObjectTypes.buildables.containsKey(clazz)) {
            Buildable b = ObjectTypes.buildables.get(clazz);
            try {
                return ObjectTypes.buildables.get(clazz).getSaveData(o);
            } catch (Exception e) {
                e.printStackTrace();
                if (b != null) {
                    Error error = new Error(ErrorType.Issue_Generic)
                            .addMessage("Exception produced on using buildable '" + b.getClass().getName() + "' located in " + b.getClass().getPackage())
                            .addObject(b).complete();
                    error.send();
                    return error;
                }
                Error error = new Error(ErrorType.Issue_Generic).addMessage(e.getMessage()).complete();
                error.send();
                return error;
            }
        } else {
            return new Error(ErrorType.Non_Buildable_Object).addMessage("No buildable found for " + clazz.getName()).addObject(o.getClass().getName()).complete();
        }
    }


    public Object getSaveData(Object o, Field field) {
        Object obj = locate(o);
        if (obj instanceof Error) return obj;
        Response response = (Response) obj;
        if (response.isComplex()) {
            return new SaveData(ObjectTypes.complexBuildables.get(response.getTargetClass()).getSaveData(o, field, this));
        }
        return new SaveData(ObjectTypes.buildables.get(response.getTargetClass()).getSaveData(o));
    }
    //Give collection or string from disk as o
    public Object getLoadedObject(Object o, Field field) {
        Object obj = locate(o);
        if (obj instanceof Error) return obj;
        Response response = (Response) obj;
        if (response.isComplex()) {
            return new LoadedObject(
                    ObjectTypes.complexBuildables.get(response.getTargetClass()).build((Collection<String>) o, o, field, this),
                    ObjectTypes.complexBuildables.get(response.getTargetClass())
            );
        }
        return new LoadedObject(
                ObjectTypes.buildables.get(response.getTargetClass()).build((String) o),
                ObjectTypes.buildables.get(response.getTargetClass())
        );

    }

    private Object locate(Object o) {
        Response simple = locate(o, ObjectTypes.buildables, false);
        Response complexx = locate(o, ObjectTypes.complexBuildables, true);
        if (simple.getPrecision() == Precision.None && complexx.getPrecision() == Precision.None) {
            return new Error(ErrorType.Non_Buildable_Object).addObject(o)
                    .addMessage("A builder could not be found for Object '" + o.getClass().getName() + "' in package " + o.getClass().getPackage().getName())
                    .complete();
        }
        if (simple.getPrecision().i() >= complexx.getPrecision().i()) {
            return simple;
        } else {
            return complexx;
        }
    }

    private void setField(Field field, Object o, LoadedObject loadedObject) throws IllegalAccessException {
        field.set(o, loadedObject.getObject());
    }
    public void loadEdits(PersistenceObject o, Adapter a) {
        for (Map.Entry<String, String> entry : o.getRemoteEdits().entrySet()) {
            try {
                Field f =  o.getObject().getClass().getField(entry.getKey());
                Object lo = getLoadedObject(entry.getValue(), f);
                if (!(lo instanceof Error)) {
                    if (lo == null && Config.safeEdit) {
                        Error error = new Error(ErrorType.Null_Object)
                                .addMessage("Could not load object successfully for field '" + entry.getKey() + "' using '" + entry.getValue()
                                        + "' on '" + o.getProjectIdentifier() + " " + o.getClassIdentifier() + "_" + o.getObjectIdentifier()
                                        + "' if it was intended to set to null please disable safeEdit in config.").complete();
                        a.getErrorMap().put(error, false);
                        error.send();
                    } else {
                        f.set(o.getObject(), ((LoadedObject) lo).getObject());
                    }
                } else {
                    a.getErrorMap().put((Error) lo, false);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Error error = new Error(ErrorType.Issue_Generic).addMessage("Could not set field '" + entry.getKey() + "' on PersistenceObject " + o.getProjectIdentifier() + " " + o.getClassIdentifier() + "_" + o.getObjectIdentifier()).complete();
                a.getErrorMap().put(error, false);
                error.send();
                a.getExceptionMap().put(e, false);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                Error error = new Error(ErrorType.No_Field).addMessage("Could not find field '" + entry.getKey() + "' on PersistenceObject " + o.getProjectIdentifier() + " " + o.getClassIdentifier() + "_" + o.getObjectIdentifier()).complete();
                a.getErrorMap().put(error, false);
                error.send();
                a.getExceptionMap().put(e, false);
            }
        }
    }

}
