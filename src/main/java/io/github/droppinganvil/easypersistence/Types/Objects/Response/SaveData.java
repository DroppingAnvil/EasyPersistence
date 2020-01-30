package io.github.droppinganvil.easypersistence.Types.Objects.Response;

import java.util.Collection;

public class SaveData {
    private Collection<String> collection;
    private String string;
    private SaveDataType saveDataType;

    public SaveData(Object o) {
        if (o instanceof Collection) {
            collection = (Collection) o;
            saveDataType = SaveDataType.Collection;
            return;
        }
        string = (String) o;
        saveDataType = SaveDataType.String;
    }

    public Collection<String> getCollection() {
        return collection;
    }

    public String getString() {
        return string;
    }

    public SaveDataType getSaveDataType() {
        return saveDataType;
    }

    public Object getData() {
        if (collection == null) {
            return string;
        }
        return collection;
    }
}
