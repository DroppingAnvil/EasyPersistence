package io.github.droppinganvil.easypersistence.Panel.Actions;

import io.github.droppinganvil.easypersistence.PersistenceManager;
import io.github.droppinganvil.easypersistence.PersistenceObject;
import io.github.droppinganvil.easypersistence.PersistenceUser;
import io.github.droppinganvil.easypersistence.Types.Objects.ObjectTypes;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class getEditables extends Verifier implements Action{
    //TODO try cast too, support complex
    public String getAction() {
        return Operations.GET_EDITABLES.name();
    }

    public String parse(String data) {
        String[] sData = data.split("@");
        if (!verify(sData, 5)) return Invalids.INVALID.name();
        PersistenceUser pU = PersistenceManager.getUser(sData[1]);
        if (pU == null) return Invalids.INVALID_PROJECT.name();
        PersistenceObject pO = pU.getOwnedObjects().get(sData[2] + "_" + sData[3]);
        if (pO == null) return Invalids.INVALID_OBJECT.name();
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        for (Field f : pO.getObject().getClass().getDeclaredFields()) {
            try {
                if (f.isAccessible() && ObjectTypes.buildables.containsKey(f.get(pO.getObject()).getClass())) {
                    map.put(f.getName(), f.get(pO.getObject()).getClass().getSimpleName());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return Invalids.INVALID.name();
            }
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e : map.entrySet()) {
            //Chained looks ridiculous lol
            sb.append(" {").append(e.getKey()).append(",").append(e.getValue()).append("}");
        }
        return toClient.stringify(getAction(), sData[1], sData[2] + "_" + sData[3], sb.toString(), "None");
    }
}
