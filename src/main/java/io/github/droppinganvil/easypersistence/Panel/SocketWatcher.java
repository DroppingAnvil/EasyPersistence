package io.github.droppinganvil.easypersistence.Panel;

import io.github.droppinganvil.easypersistence.Configuration.Config;
import io.github.droppinganvil.easypersistence.Panel.Actions.Actions;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketWatcher implements Runnable {
    public static Boolean enabled = true;
    public void run() {
        while (enabled) {
            try {
                Socket socket = NetworkManager.serverSocket.accept();
                if (addressCheck(socket)) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String[] args = in.readLine().split("@");
                    if (args.length != 0) {
                        new DataOutputStream(socket.getOutputStream()).writeBytes(Actions.actions.get(args[0]).parse(in.readLine()));
                    }

                }
            } catch (IOException ignored) {}
            if (Config.wait != 0) {
                try {
                    wait(Config.wait);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private Boolean addressCheck(Socket socket) {
        if (Config.addressWhitelist) {
            return Config.addressList.contains(socket.getInetAddress().toString());
        }
        return true;
    }
}
