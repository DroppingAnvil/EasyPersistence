package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;

import java.util.HashMap;

public class PersistenceUser {
    private Class userClass;
    private String preferredName;
    private HashMap<Integer, Class> classMap;
    private HashMap<Integer, PersistenceObject> ownedObjects;

    public PersistenceUser(Class owner, String name) {
        this.userClass = owner;
        this.preferredName = name;
        this.ownedObjects = new HashMap<Integer, PersistenceObject>();
    }

    public final void registerUser(String identifier, Class def) {
    }

    public final void registerObject(Integer objectID, PersistenceObject object) {

    }

    public void handleLocalError(Error error) {
        System.out.print("[" + preferredName + "]" + " [" + error.getErrorType() + "]" + " [" + error.getMessage() + "]");
    }

    public void handleGlobalError(Error error) {

    }

    public void handleInfo(Info info) {

    }

}
