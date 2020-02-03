package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Configuration.User;

public class EasyPersistence {
    public void main() {
        PersistenceManager.load();
        new User();
    }
}
