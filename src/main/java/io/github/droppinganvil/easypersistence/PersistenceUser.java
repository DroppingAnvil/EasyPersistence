package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.Types.Objects.Adapter;
import io.github.droppinganvil.easypersistence.Types.YAML.YAMLAdapter;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceUser {
    private String preferredName;
    private ConcurrentHashMap<String, PersistenceObject> ownedObjects;
    private File directory;
    private Adapter adapter;
    private Boolean enabled = false;

    public PersistenceUser(String name) {
        this.preferredName = name;
        this.ownedObjects = new ConcurrentHashMap<String, PersistenceObject>();
        this.directory = new File(getProjectIdentifier());
        this.adapter = new YAMLAdapter();
        //Register
        registerUser(name);
    }

    public PersistenceUser(String name, File directory, Adapter adapter) {
        this.preferredName = name;
        this.ownedObjects = new ConcurrentHashMap<String, PersistenceObject>();
        this.directory = directory;
        this.adapter = adapter;
        //Register
        registerUser(name);
    }

    public final void cycle() {
        for (PersistenceObject po : ownedObjects.values()) {
            if (po.cycle()) {
                if (po.getLoaded()) {
                    adapter.saveObject(po);
                } else {
                    adapter.loadObject(po);
                    po.setLoaded();
                }
            }
        }
    }

    public final void registerUser(String identifier) {
        if (!directory.exists()) {
            new Info(InfoType.Writing_File, Level.File).addMessage("Directory did not exist creating it now.").complete().send();
            if (!directory.mkdir()) new Error(ErrorType.Directory_Creation_Failed).addObject(this).complete().send();
        }
        if (PersistenceManager.registerUser(identifier, this)) enabled = true;
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

    public final ConcurrentHashMap<String, PersistenceObject> getOwnedObjects() {
        return ownedObjects;
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
