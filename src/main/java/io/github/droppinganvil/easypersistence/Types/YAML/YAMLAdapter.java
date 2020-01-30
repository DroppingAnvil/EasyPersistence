package io.github.droppinganvil.easypersistence.Types.YAML;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Adapter;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.SaveData;
import io.github.droppinganvil.easypersistence.Types.Objects.Status.State;
import io.github.droppinganvil.easypersistence.Types.Objects.Status.Status;
import io.github.droppinganvil.easypersistence.Types.TypeAdapter;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class YAMLAdapter extends TypeAdapter implements Adapter {
    private State state;
    private String s;
    private ConcurrentHashMap<Exception, Boolean> exceptions = new ConcurrentHashMap<Exception, Boolean>();
    private ConcurrentHashMap<Error, Boolean> errors = new ConcurrentHashMap<Error, Boolean>();
    private Yaml yaml = new Yaml();
    public YAMLAdapter() {
    }

    public void loadObject(PersistenceObject o) {
        try {
            changeState(State.Read, null);
            s = o.getFile().getPath();
            if (!o.getFile().exists()) {
                new Info(InfoType.Writing_File, Level.File).addMessage("File did not exist.").addUser(o.getUser()).complete().send();
                changeState(State.Write, null);
                if (!o.getFile().createNewFile()) {
                    Error error = new Error(ErrorType.File_Unknown).addUser(o.getUser()).complete();
                    errors.put(error, false);
                    error.send();
                    changeState(State.Issue, error);
                    return;
                }
            }
                changeState(State.Read, null);
                LinkedHashMap<String, Object> root = yaml.load(new FileInputStream(o.getFile()));
                for (String field : root.keySet()) {
                    Object ob = root.get(field);
                    Field f = null;
                    try {
                        f = o.getObject().getClass().getField(field);
                    } catch (NoSuchFieldException ex) {
                        ex.printStackTrace();
                        Error error = new Error(ErrorType.No_Field)
                                .addMessage("A field with the name " + field + " could not be found")
                                .addUser(o.getUser()).complete();
                        errors.put(error, false);
                        error.send();
                        changeState(State.Issue, error);
                    }
                    if (f != null) {
                        try {
                            Error error = load(o.getObject(), ob, f);
                            if (error != null) {
                                errors.put(error, false);
                                error.send();
                                changeState(State.Issue, error);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            exceptions.put(e, false);
                            changeState(State.Critical, e);
                        }
                    }
                }
        } catch (Exception e) {
            exceptions.put(e, false);
            e.printStackTrace();
            changeState(State.Critical, null);
        }
        changeState(State.Idle, null);
    }

    public void saveObject(PersistenceObject o) {
        try {
            changeState(State.Write, null);
            s = o.getFile().getPath();
            if (!o.getFile().exists()) {
                new Info(InfoType.Writing_File, Level.File).addMessage("File did not exist.").addUser(o.getUser()).complete().send();
                if (!o.getFile().createNewFile()) {
                    Error error = new Error(ErrorType.File_Unknown).addUser(o.getUser()).complete();
                    errors.put(error, false);
                    error.send();
                    changeState(State.Issue, error);
                    return;
                }
            }
            changeState(State.Read, null);
            LinkedHashMap<String, Object> data = yaml.load(new FileInputStream(o.getFile()));
            for (Field field : o.getObject().getClass().getDeclaredFields()) {
                if (field.isAccessible()) {
                    Object obj = getSaveData(field.get(o.getObject()), field);
                    if (!(obj instanceof Error)) {
                        if (((SaveData) obj).getData() != null) {
                            if (data.containsKey(field.getName())) {
                                data.remove(field.getName());
                            }
                            data.put(field.getName(), ((SaveData) obj).getData());
                        } else {
                            Error error = new Error(ErrorType.Null_Object).addObject(obj)
                                    .addMessage("Data for field '" + field.getName() + "' on '" + o.getObject().getClass().getName() + "' came back null")
                                    .complete();
                            errors.put(error, false);
                            error.send();
                            changeState(State.Issue, error);
                        }
                    } else {
                        errors.put((Error) obj, false);
                        changeState(State.Issue, obj);
                    }
            }
            }
            FileWriter writer = new FileWriter(o.getFile());
            yaml.dump(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
            exceptions.put(e, false);
            changeState(State.Critical, e);
        }
        changeState(State.Idle, null);
    }

    public Status getStatus() {
        return new Status(state, s);
    }

    public void clear() {
        for (Exception e : exceptions.keySet()) {
            boolean b = exceptions.get(e);
            if (!b) {
                exceptions.replace(e, false, true);
            }
        }
        for (Error e : errors.keySet()) {
            boolean b = errors.get(e);
            if (!b) {
                errors.replace(e, false, true);
            }
        }
    }
    private void changeState(State state, Object obj) {
        this.state = state;
        //Pass on obj (Error, Exception or null)
        //TODO Web
    }
}
