package io.github.droppinganvil.easypersistence.Configuration;

import io.github.droppinganvil.easypersistence.PersistenceUser;

public class User extends PersistenceUser {
    private static User instance;
    public static User getInstance() {
        return instance;
    }
    public User() {
        super("EasyPersistence");
        if (instance == null) instance = this;
    }

}
