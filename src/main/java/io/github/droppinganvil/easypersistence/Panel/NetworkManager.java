package io.github.droppinganvil.easypersistence.Panel;

import io.github.droppinganvil.easypersistence.Configuration.Config;
import io.github.droppinganvil.easypersistence.Panel.Actions.Action;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkManager {
    public static ServerSocket serverSocket;
    private static Boolean started = false;
    public static ConcurrentHashMap<String, Action> actions = new ConcurrentHashMap<String, Action>();

    static {
        try {
            serverSocket = new ServerSocket(Config.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() {

    }


}
