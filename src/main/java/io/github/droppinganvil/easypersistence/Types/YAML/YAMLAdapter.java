package io.github.droppinganvil.easypersistence.Types.YAML;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.Types.Objects.Adapter;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.SaveData;
import io.github.droppinganvil.easypersistence.Types.Objects.Response.SaveDataType;
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
            state = State.Read;
            s = o.getFile().getPath();
            if (!o.getFile().exists()) {
                new Info(InfoType.Writing_File, Level.File).addMessage("File did not exist.").addUser(o.getUser()).complete().send();
                state = State.Write;
                if (!o.getFile().createNewFile()) {
                    Error error = new Error(ErrorType.File_Unknown).addUser(o.getUser()).complete();
                    errors.put(error, false);
                    error.send();
                    state = State.Issue;
                    return;
                }
            }
                state = State.Read;
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
                        state = State.Issue;
                    }
                    if (f != null) {
                        try {
                            load(o.getObject(), ob, f);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            exceptions.put(e, false);
                            state = State.Critical;
                        }
                    }
                }
        } catch (Exception e) {
            exceptions.put(e, false);
            e.printStackTrace();
            state = State.Critical;
        }
        if (state == State.Read) state = State.Idle;
    }

    public void saveObject(PersistenceObject o) {
        try {
            state = State.Write;
            s = o.getFile().getPath();
            if (!o.getFile().exists()) {
                new Info(InfoType.Writing_File, Level.File).addMessage("File did not exist.").addUser(o.getUser()).complete().send();
                if (!o.getFile().createNewFile()) {
                    Error error = new Error(ErrorType.File_Unknown).addUser(o.getUser()).complete();
                    errors.put(error, false);
                    error.send();
                    state = State.Issue;
                    return;
                }
            }
            LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
            for (Field field : o.getObject().getClass().getFields()) {
                SaveData saveData = getSaveData(field.get(o.getObject()));
                if (saveData != null) {
                    if (saveData.getSaveDataType() == SaveDataType.Collection) {
                        data.put(field.getName(), saveData.getCollection());
                    } else {
                        data.put(field.getName(), saveData.getString());
                    }
                    FileWriter writer = new FileWriter(o.getFile());
                    yaml.dump(data, writer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exceptions.put(e, false);
            state = State.Critical;
        }
        if (state == State.Write) state = State.Idle;
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
}
