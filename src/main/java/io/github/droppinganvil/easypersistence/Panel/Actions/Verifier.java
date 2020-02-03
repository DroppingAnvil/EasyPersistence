package io.github.droppinganvil.easypersistence.Panel.Actions;

import io.github.droppinganvil.easypersistence.Configuration.Config;

public class Verifier {
    public boolean verify(String[] strings) {
        if (strings.length != 5) return false;
        if (!strings[4].equals(Config.key)) return false;
        return true;
    }
}
