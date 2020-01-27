package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.Types.Objects.Adapter;
import io.github.droppinganvil.easypersistence.Types.YAMLAdapter;

import java.io.File;
import java.util.HashMap;

public class PersistenceUser {
    private Class<?> userClass;
    private String preferredName;
    private HashMap<String, Class<?>> classMap;
    private HashMap<String, PersistenceObject> ownedObjects;
    private File directory;
    private Adapter adapter;

    public PersistenceUser(Class<?> owner, String name) {
        this.userClass = owner;
        this.preferredName = name;
        this.ownedObjects = new HashMap<String, PersistenceObject>();
        this.classMap = new HashMap<String, Class<?>>();
        this.directory = new File(getProjectIdentifier());
        this.adapter = new YAMLAdapter();
    }

    public PersistenceUser(Class<?> owner, String name, File directory, Adapter adapter) {
        this.userClass = owner;
        this.preferredName = name;
        this.ownedObjects = new HashMap<String, PersistenceObject>();
        this.classMap = new HashMap<String, Class<?>>();
        this.directory = directory;
        this.adapter = adapter;
    }

    public final void registerUser(String identifier) {
        if (!directory.exists()) {
            new Info(InfoType.Writing_File, Level.File).addMessage("Directory did not exist creating it now.").complete().send();
            if (!directory.mkdir()) new Error(ErrorType.Directory_Creation_Failed).addObject(this).complete().send();
        }
        PersistenceManager.registerUser(identifier, this);
    }

    public final void registerObject(String classID, String objectID, PersistenceObject object) {
        ownedObjects.put(classID + "_" + objectID, object);
    }

    public void handleLocalError(Error error) {
        System.out.print("[" + preferredName + "]" + " [" + error.getErrorType() + "]" + " [" + error.getMessage() + "]");
    }

    public void handleGlobalError(Error error) {

    }

    public void handleInfo(Info info) {

    }

    public final HashMap<String, Class<?>> getClassMap() {
        return classMap;
    }

    public final HashMap<String, PersistenceObject> getOwnedObjects() {
        return ownedObjects;
    }

    public final Class<?> getUserClass() {
        return userClass;
    }

    public void registerClass(String classID, Class<?> def) {
        classMap.put(classID, def);
    }

    public final String getProjectIdentifier() {
        return preferredName;
    }

    public Boolean isClassSaved(String classID) {
        return new File(directory, classID).exists();
    }

    public File getDirectory() {
        return directory;
    }

    public Adapter getAdapter() {
        return adapter;
    }
}
