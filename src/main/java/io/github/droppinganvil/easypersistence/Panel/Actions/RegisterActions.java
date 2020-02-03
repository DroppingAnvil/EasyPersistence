package io.github.droppinganvil.easypersistence.Panel.Actions;

import io.github.droppinganvil.easypersistence.Panel.NetworkManager;

public class RegisterActions {
    public static void register() {
        NetworkManager.actions.put("GET_EDITABLES", new getEditables());
    }
}
