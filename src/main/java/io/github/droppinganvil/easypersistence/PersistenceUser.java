package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;

import java.io.File;
import java.util.HashMap;

public class PersistenceUser {
    private Class<?> userClass;
    private String preferredName;
    private HashMap<Integer, Class<?>> classMap;
    private HashMap<Integer, PersistenceObject> ownedObjects;
    private File directory;

    public PersistenceUser(Class<?> owner, String name) {
        this.userClass = owner;
        this.preferredName = name;
        this.ownedObjects = new HashMap<Integer, PersistenceObject>();
        this.classMap = new HashMap<Integer, Class<?>>();
        this.directory = new File(getProjectIdentifier());
    }

    public PersistenceUser(Class<?> owner, String name, File directory) {
        this.userClass = owner;
        this.preferredName = name;
        this.ownedObjects = new HashMap<Integer, PersistenceObject>();
        this.classMap = new HashMap<Integer, Class<?>>();
        this.directory = directory;
    }

    public final void registerUser(String identifier) {
        if (!directory.exists()) {
            new Info(InfoType.Writing_File, Level.File).addMessage("Directory did not exist creating it now.").complete().send();
            if (!directory.mkdir()) new Error(ErrorType.Directory_Creation_Failed).addObject(this).complete().send();
        }
    }

    public final void registerObject(String objectID, PersistenceObject object) {
        // objectIDs will by done simply by classID_objectID
    }

    public void handleLocalError(Error error) {
        System.out.print("[" + preferredName + "]" + " [" + error.getErrorType() + "]" + " [" + error.getMessage() + "]");
    }

    public void handleGlobalError(Error error) {

    }

    public void handleInfo(Info info) {

    }

    public final HashMap<Integer, Class<?>> getClassMap() {
        return classMap;
    }

    public final HashMap<Integer, PersistenceObject> getOwnedObjects() {
        return ownedObjects;
    }

    public final Class<?> getUserClass() {
        return userClass;
    }

    public void registerClass(Integer classID, Class<?> def) {
        classMap.put(classID, def);
    }

    public final String getProjectIdentifier() {
        return preferredName;
    }
}
