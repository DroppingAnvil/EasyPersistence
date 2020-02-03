package io.github.droppinganvil.easypersistence.Panel;

import io.github.droppinganvil.easypersistence.Configuration.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class SocketWatcher implements Runnable {
    public Boolean enabled = true;
    public void run() {
        while (enabled) {
            try {
                Socket socket = NetworkManager.serverSocket.accept();
                if (addressCheck(socket)) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String[] args = in.readLine().split("@");
                    if (args.length != 0) {

                    }

                }
            } catch (IOException ignored) {}
        }
    }
    private Boolean addressCheck(Socket socket) {
        if (Config.addressWhitelist) {
            return Config.addressList.contains(socket.getInetAddress().toString());
        }
        return true;
    }
}
