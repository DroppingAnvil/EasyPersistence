package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Configuration.User;
import io.github.droppinganvil.easypersistence.Panel.NetworkManager;

public class EasyPersistence {
    public void main() {
        PersistenceManager.load();
        new User();
        NetworkManager.start();
    }
    public void stop() {
        NetworkManager.stop();
    }
}
