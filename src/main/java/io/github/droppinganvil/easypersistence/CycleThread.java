package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Configuration.Config;

public class CycleThread implements Runnable {
    public static boolean enabled = true;
    //TODO Thread per user
    public void run() {
        while (enabled) {
            for (PersistenceUser persistenceUser : PersistenceManager.getUsers()) {
                persistenceUser.cycle();
            }
            try {
                Thread.sleep(Config.cycleInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
