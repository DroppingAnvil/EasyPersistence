package io.github.droppinganvil.easypersistence.Notifications.Info;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.PersistenceManager;
import io.github.droppinganvil.easypersistence.PersistenceUser;

import java.util.HashSet;

public class Info {
    private InfoType type;
    private HashSet<ExtraData> extra;
    private Level level;
    private PersistenceUser user;
    private String message;
    private Boolean isComplete = false;

    public Info(InfoType type, Level level) {
        this.type = type;
        this.level = level;
    }

    public InfoType getType() {
        return type;
    }

    public Level getLevel() {
        return level;
    }

    public HashSet<ExtraData> getExtra() {
        return extra;
    }

    public String getMessage() {return message;}

    public PersistenceUser getUser() {return user;}

    public Info addMessage(String message) {
        if (checkEdit()) return this;
        this.message = message;
        if (this.message != null) {
            addFlag(ExtraData.Message);
        }
        return this;
    }

    public Info addUser(PersistenceUser user) {
        if (checkEdit()) return this;
        this.user = user;
        if (this.user != null) {
            addFlag(ExtraData.User);
        }
        return this;
    }

    public Boolean hasFlags() {
        return extra != null;
    }

    private Info addFlag(ExtraData extra) {
        if (this.extra == null) {
            this.extra = new HashSet<ExtraData>();
        }
        this.extra.add(extra);
        return this;
    }

    public Boolean hasFlag(ExtraData extra) {
        if (this.extra != null) return this.extra.contains(extra);
        return false;
    }

    public Info complete() {
        if (checkEdit()) return this;
        isComplete = true;
        return this;
    }

    public Boolean isCompleted() {
        return isComplete;
    }

    private Boolean checkEdit() {
        if (isComplete) {
            new Error(ErrorType.Edit_After_Complete).addObject(this).complete().send();
            return true;
        }
        return false;
    }

    public void send() {
        PersistenceManager.sendInfo(this);
    }

}
