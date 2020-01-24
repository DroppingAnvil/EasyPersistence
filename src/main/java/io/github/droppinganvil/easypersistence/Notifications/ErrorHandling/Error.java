package io.github.droppinganvil.easypersistence.Notifications.ErrorHandling;

import io.github.droppinganvil.easypersistence.PersistenceManager;
import io.github.droppinganvil.easypersistence.PersistenceUser;

import java.util.HashSet;

public class Error {
    private ErrorType errorType;
    private String message;
    private PersistenceUser user;
    private HashSet<ExtraErrorData> extra;
    private Object subject;
    private Boolean isComplete = false;

    public Error(ErrorType errorType) {
        this.errorType = errorType;
    }

    public ErrorType getErrorType() {
        return errorType;
    }
    public String getMessage() {
        return message;
    }
    public PersistenceUser getUser() {
        return user;
    }
    public Object getSubject() {return subject;}
    public Boolean hasFlags() {return extra != null;}
    public Error complete() {
        if (checkComplete()) return this;
        isComplete = true;
        return this;
    }
    public Error addMessage(String message) {
        if (checkComplete() || message == null) return this;
        this.message = message;
        addFlag(ExtraErrorData.Message);
        return this;
    }
    public Error addUser(PersistenceUser user) {
        if (checkComplete() || message == null) return this;
        this.user = user;
        addFlag(ExtraErrorData.User);
        return this;
    }
    public Error addObject(Object subject) {
        if (checkComplete() || subject == null) return this;
        this.subject = subject;
        addFlag(ExtraErrorData.Object);
        return this;
    }
    private void addFlag(ExtraErrorData extra) {
        if (this.extra == null) this.extra = new HashSet<ExtraErrorData>();
        this.extra.add(extra);
    }
    public void send() {
        if (!isComplete) {
            new Error(ErrorType.Sent_Before_Complete).addObject(this).addMessage("Notifications must be completed before being sent!")
                    .complete().send();
            return;
        }
        if (hasFlag(ExtraErrorData.User)) {
            user.handleLocalError(this);
            return;
        }
        PersistenceManager.sendGlobalError(this);
    }
    public Boolean hasFlag(ExtraErrorData extra) {
        if (this.extra == null) return false;
        return this.extra.contains(extra);
    }
    private Boolean checkComplete() {
        if (isComplete) {
            new Error(ErrorType.Edit_After_Complete).addObject(this).addMessage("Notifications cannot be edited after completed!")
                    .complete().send();
            return true;
        }
        return false;
    }
}
