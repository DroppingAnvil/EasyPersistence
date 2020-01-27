package io.github.droppinganvil.easypersistence.Types.Objects;

import io.github.droppinganvil.easypersistence.Types.Objects.Response.SaveData;

public interface Writable {
    /**
     * write is called to write the entire memory file onto the disk. Please account for IO related issues.
     */
    void write();

    /**
     * save is called to save a field from an object into the memory file.
     * @param field File Location/Key
     * @param data Data that should be written to the Location/Key. Please note that the actual data in SaveData can be
     *             a String Collection or a String you can determine which one by using SaveData.getSaveDataType()
     */
    void save(String field, SaveData data);
}
