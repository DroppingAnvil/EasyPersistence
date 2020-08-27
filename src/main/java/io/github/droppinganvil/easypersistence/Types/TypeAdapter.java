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
import io.github.droppinganvil.easypersistence.Types.Objects.Response.*;

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
    public Response locate(Object o, HashMap<Class<?>, ?> map, Type type) {
        if (map.containsKey(o.getClass())) return new Response(Precision.Exact, o.getClass(), type);
        for (Class<?> c : map.keySet()) {
            if (canCast(c, o)) {
                return new Response(Precision.Cast, c, type);
            }
        }
        return new Response(Precision.None, null, type);
    }

    public Response locate(Class<?> clazz, HashMap<Class<?>, ?> map, Type type) {
        if (map.containsKey(clazz)) return new Response(Precision.Exact, clazz, type);
        for (Class<?> c : map.keySet()) {
            if (canCast(c, clazz)) {
                return new Response(Precision.Cast, c, type);
            }
        }
        return new Response(Precision.None, null, type);
    }

    public Error load(Object targetMain, Object parserValue, Field field) throws IllegalAccessException {
        Object loadedOrError = getLoadedObject(parserValue, field, field.get(targetMain));
        if (loadedOrError instanceof Error) return (Error) loadedOrError;
            setField(field, targetMain, (LoadedObject) loadedOrError);
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
        switch (response.getType()) {
            case Simple: return new SaveData(ObjectTypes.buildables.get(response.getTargetClass()).getSaveData(o));
            case Complex: return new SaveData(ObjectTypes.complexBuildables.get(response.getTargetClass()).getSaveData(o, field, this));
            case Mapped: return new SaveData(ObjectTypes.mappedBuildables.get(response.getTargetClass()).getSaveData(o, field, this));
        }
        return null;
    }
    //Give collection or string from disk as o
    public Object getLoadedObject(Object parserVal, Field field, Object fieldVal) {
        Object locateResult = locate(fieldVal);
        if (locateResult instanceof Error) return locateResult;
        Response response = (Response) locateResult;
        switch (response.getType()) {
            case Simple:         return new LoadedObject(
                    ObjectTypes.buildables.get(response.getTargetClass()).build((String) parserVal),
                    ObjectTypes.buildables.get(response.getTargetClass()));
            case Complex:            return new LoadedObject(
                    ObjectTypes.complexBuildables.get(response.getTargetClass()).build((Collection<String>) parserVal, parserVal, field, this),
                    ObjectTypes.complexBuildables.get(response.getTargetClass())
            );
            case Mapped:             return new LoadedObject(
                    ObjectTypes.mappedBuildables.get(response.getTargetClass()).build((Map<String, String>) parserVal, parserVal, field, this),
                    ObjectTypes.mappedBuildables.get(response.getTargetClass())
            );
        }
        return null;
    }
    public Object getLoadedObject(Object parserVal, Class<?> clazz) {
        Object locateResult = locate((Class<?>) clazz);
        if (locateResult instanceof Error) return locateResult;
        Response response = (Response) locateResult;
        switch (response.getType()) {
            case Simple:         return new LoadedObject(
                    ObjectTypes.buildables.get(response.getTargetClass()).build((String) parserVal),
                    ObjectTypes.buildables.get(response.getTargetClass()));
            case Complex:            return new LoadedObject(
                    ObjectTypes.complexBuildables.get(response.getTargetClass()).build((Collection<String>) parserVal, parserVal, null, this),
                    ObjectTypes.complexBuildables.get(response.getTargetClass())
            );
            case Mapped:             return new LoadedObject(
                    ObjectTypes.mappedBuildables.get(response.getTargetClass()).build((Map<String, String>) parserVal, parserVal,null, this),
                    ObjectTypes.mappedBuildables.get(response.getTargetClass())
            );
        }
        return null;

    }

    private Object locate(Object o) {
        Response simple = locate(o, ObjectTypes.buildables, Type.Simple);
        Response complexx = locate(o, ObjectTypes.complexBuildables, Type.Complex);
        Response mapped = locate(o, ObjectTypes.complexBuildables, Type.Mapped);
        if (simple.getPrecision() == Precision.None && complexx.getPrecision() == Precision.None) {
            return new Error(ErrorType.Non_Buildable_Object).addObject(o)
                    .addMessage("A builder could not be found for Object '" + o.getClass().getName() + "' in package " + o.getClass().getPackage().getName())
                    .complete();
        }
        if (mapped.getPrecision().i() >= complexx.getPrecision().i()) {
            return mapped;
        } else if (complexx.getPrecision().i() >= simple.getPrecision().i()){
            return complexx;
        } else {
            return simple;
        }
    }

    private Object locate(Class<?> clazz) {
        Response simple = locate((Class<?>) clazz, ObjectTypes.buildables, Type.Simple);
        Response complexx = locate((Class<?>) clazz, ObjectTypes.complexBuildables, Type.Complex);
        Response mapped = locate((Class<?>) clazz, ObjectTypes.complexBuildables, Type.Mapped);
        if (simple.getPrecision() == Precision.None && complexx.getPrecision() == Precision.None) {
            return new Error(ErrorType.Non_Buildable_Object).addObject(clazz)
                    .addMessage("A builder could not be found for Class '" + clazz + "' in package " + clazz.getPackage().getName())
                    .complete();
        }
        if (mapped.getPrecision().i() >= complexx.getPrecision().i()) {
            return mapped;
        } else if (complexx.getPrecision().i() >= simple.getPrecision().i()){
            return complexx;
        } else {
            return simple;
        }
    }

    private void setField(Field field, Object targetObj, LoadedObject loadedObject) throws IllegalAccessException {
        field.set(targetObj, loadedObject.getObject());
    }
    public void loadEdits(PersistenceObject o, Adapter a) {
        for (Map.Entry<String, String> entry : o.getRemoteEdits().entrySet()) {
            try {
                Field f =  o.getObject().getClass().getField(entry.getKey());
                Object lo = getLoadedObject(entry.getValue(), f, f.get(o.getObject()));
                if (!(lo instanceof Error)) {
                    if (((LoadedObject) lo).getObject() == null && Config.safeEdit) {
                        Error error = new Error(ErrorType.Null_Object)
                                .addMessage("Could not load object successfully for field '" + entry.getKey() + "' using '" + entry.getValue()
                                        + "' on '" + o.getProjectIdentifier() + " " + o.getClassIdentifier() + "_" + o.getObjectIdentifier()
                                        + "' if it was intended to set to null please disable safeEdit in config.")
                                .addUser(o.getUser())
                                .addObject(o)
                                .complete();
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
                Error error = new Error(ErrorType.Issue_Generic).addMessage("Could not set field '" + entry.getKey() + "' on PersistenceObject " + o.getProjectIdentifier() + " " + o.getClassIdentifier() + "_" + o.getObjectIdentifier())
                        .addUser(o.getUser())
                        .addObject(o)
                        .complete();
                a.getErrorMap().put(error, false);
                error.send();
                a.getExceptionMap().put(e, false);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                Error error = new Error(ErrorType.No_Field).addMessage("Could not find field '" + entry.getKey() + "' on PersistenceObject " + o.getProjectIdentifier() + " " + o.getClassIdentifier() + "_" + o.getObjectIdentifier())
                        .addUser(o.getUser())
                        .addObject(o)
                        .complete();
                a.getErrorMap().put(error, false);
                error.send();
                a.getExceptionMap().put(e, false);
            }
        }
    }

}
