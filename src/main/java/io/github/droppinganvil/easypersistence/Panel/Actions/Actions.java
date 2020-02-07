package io.github.droppinganvil.easypersistence.Panel.Actions;

import java.util.concurrent.ConcurrentHashMap;

public class Actions {
    public static ConcurrentHashMap<String, Action> actions = new ConcurrentHashMap<String, Action>();
    public static void register(Action a) {
        actions.put(a.getAction(), a);
    }
    public static void registerDefaults() {
        register(new getEditables());
        register(new EditField());
    }
}
