package io.github.droppinganvil.easypersistence.Panel.Actions;

import io.github.droppinganvil.easypersistence.Configuration.Config;

public class Verifier {
    public boolean verify(String[] strings, Integer i) {
        if (strings.length != i) return false;
        if (!strings[i - 1].equals(Config.key)) return false;
        return true;
    }
}
