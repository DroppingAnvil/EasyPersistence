package io.github.droppinganvil.easypersistence.Panel.Actions;

import io.github.droppinganvil.easypersistence.PersistenceManager;
import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.PersistenceUser;

public class EditField extends Verifier implements Action {

    public String getAction() {
        return Operations.EDIT_FIELD.name();
    }

    public String parse(String data) {
        String[] sData = data.split("@");
        if (!verify(sData, 6)) return Invalids.INVALID.name();
        PersistenceUser pU = PersistenceManager.getUser(sData[1]);
        if (pU == null) return Invalids.INVALID_PROJECT.name();
        PersistenceObject pO = pU.getOwnedObjects().get(sData[2]);
        if (pO == null) return Invalids.INVALID_OBJECT.name();
        return null;
    }
}
