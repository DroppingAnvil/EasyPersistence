package io.github.droppinganvil.easypersistence.Panel.Actions;

import io.github.droppinganvil.easypersistence.Configuration.Config;

public class Verifier {
    public boolean verify(String[] strings, Integer i) {
        //So we dont leave the client hanging if there is an issue
        try {
            if (strings.length != i) return false;
            return strings[i - 1].equals(Config.key);
        } catch (Exception e) {
            return false;
        }
    }
}
