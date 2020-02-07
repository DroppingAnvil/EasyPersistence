package io.github.droppinganvil.easypersistence.Panel;

import io.github.droppinganvil.easypersistence.Configuration.Config;
import io.github.droppinganvil.easypersistence.Panel.Actions.Action;
import io.github.droppinganvil.easypersistence.Panel.Actions.Actions;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkManager {
    public static ServerSocket serverSocket;
    private static Boolean started = false;
    public static SocketWatcher sw;
    public static Thread socketWatcherThread;
    public static ConcurrentHashMap<String, Action> actions = new ConcurrentHashMap<String, Action>();

    public static void start() {
        Actions.registerDefaults();
        try {
            serverSocket = new ServerSocket(Config.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sw = new SocketWatcher();
        socketWatcherThread = new Thread(sw);
        socketWatcherThread.start();
    }

    public static void stop() {
        SocketWatcher.enabled = false;
    }


}
