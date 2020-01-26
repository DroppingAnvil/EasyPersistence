package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Info;
import io.github.droppinganvil.easypersistence.Notifications.Info.InfoType;
import io.github.droppinganvil.easypersistence.Notifications.Info.Level;
import io.github.droppinganvil.easypersistence.Types.Objects.Register;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceManager {
    //Support multithread loading
    private final static ConcurrentHashMap<String, PersistenceUser> identifierMap = new ConcurrentHashMap<String, PersistenceUser>();
    public static Queue<PersistenceObject> largeLoad = new LinkedList<PersistenceObject>();
    //quickLoad should only be used for time critical task such as loading users data when they login largeLoad should be used if loading mass files beforehand
    public static Queue<PersistenceObject> quickLoad = new LinkedList<PersistenceObject>();

    public static PersistenceUser getUser(String s) {
        return identifierMap.get(s);
    }
    public static Boolean isIdentifierTaken(String s) {
        return identifierMap.containsKey(s);
    }
    public static Boolean isUserRegistered(PersistenceUser user) {
        return identifierMap.containsValue(user);
    }
    public static void registerUser(String identifier, PersistenceUser user) {
        if (isIdentifierTaken(identifier)) {
            new Error(ErrorType.Conflict_Identifier).addUser(user)
                    .addMessage("That identifier (" + identifier + ") is already taken")
                    .complete().send();
            return;
        }
        identifierMap.put(identifier, user);
        new Info(InfoType.New_Identifier_Registered, Level.Register)
                .addUser(user).addMessage(identifier).complete().send();
    }
    public static void sendGlobalError(Error error) {
        for (PersistenceUser user : identifierMap.values()) {
            user.handleGlobalError(error);
        }
    }
    public static void sendInfo(Info info) {
        for (PersistenceUser user : identifierMap.values()) {
            user.handleInfo(info);
        }
    }
    public static void load() {
        Register.defaults();
    }
}
