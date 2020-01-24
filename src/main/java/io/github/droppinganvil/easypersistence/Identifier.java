package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;

public class Identifier {
    private String classIdentifier;
    private Integer objectIdentifier;
    private PersistenceUser user;
    public Identifier(String classID, Integer objectID, PersistenceUser user) {
        this.classIdentifier = classID;
        this.objectIdentifier = objectID;
        this.user = user;
        if (this.user == null) new Error(ErrorType.Null_User).addMessage("user in Identifier constructor cannot be null").complete().send();
        if (classIdentifier == null) new Error(ErrorType.Null_Class_ID).addMessage("classID in Identifier constructor cannot be null")
        .addUser(user).complete().send();
        if (objectIdentifier == null) new Error(ErrorType.Null_Object_ID).addMessage("objectID in Identifier constructor cannot be null")
        .addUser(user).complete().send();
    }
}
