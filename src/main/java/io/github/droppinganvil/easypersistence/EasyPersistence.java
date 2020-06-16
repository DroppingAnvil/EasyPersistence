package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Configuration.Config;
import io.github.droppinganvil.easypersistence.Configuration.User;
import io.github.droppinganvil.easypersistence.Panel.NetworkManager;

public class EasyPersistence {
    public static void main(String[] args) {
        PersistenceManager.load();
        new User();
        new Thread(new CycleThread()).start();
        new Config();
        //TODO Continue networking development after stable beta
        //NetworkManager.start();
    }
    public void stop() {
        NetworkManager.stop();
    }
}
