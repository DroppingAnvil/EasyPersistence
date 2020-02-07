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
        if (sData[3] == null || sData[4] == null) return Invalids.INVALID_ARGS.name();
        pO.getRemoteEdits().put(sData[3], sData[4]);
        return toClient.stringify(Operations.EDIT_FIELD.name(), sData[1], sData[2], "1", "None");
    }
}
