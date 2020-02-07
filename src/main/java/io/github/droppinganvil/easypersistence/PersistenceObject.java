package io.github.droppinganvil.easypersistence;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class PersistenceObject extends Identifier {
    private Object object;
    private Integer maxCycles;
    private Integer cycles;
    private File file;
    private Boolean loaded = false;
    private ConcurrentHashMap<String, String> edits = new ConcurrentHashMap<String, String>();
    //Loading a previous OR creating new specific
    public PersistenceObject(String classID, String objectID, PersistenceUser user) {
        super(classID, objectID, user);
        this.file = new File(new File(super.getUser().getDirectory(), super.getClassIdentifier()), super.getObjectIdentifier());
        maxCycles = 3;
        cycles = 3;
    }
    public PersistenceObject(String classID, String objectID, PersistenceUser user, Integer maxCycles, Integer cycles) {
        super(classID, objectID, user);
        this.file = new File(new File(super.getUser().getDirectory(), super.getClassIdentifier()), super.getObjectIdentifier());
        this.maxCycles = maxCycles;
        this.cycles = cycles;
    }
    public PersistenceObject(String classID, String objectID, PersistenceUser user, Integer maxCycles, Integer cycles, File file) {
        super(classID, objectID, user);
        this.file = file;
        this.maxCycles = maxCycles;
        this.cycles = cycles;
    }
    public File getFile() {
        return file;
    }
    public Object getObject() {
        return object;
    }
    public Integer getCycles() {
        return cycles;
    }
    public void resetCycles() {
        cycles = maxCycles;
    }
    public Boolean cycle() {
        if (cycles > 0) {
            cycles--;
            return false;
        } else {
            resetCycles();
            return true;
        }
    }
    public void setLoaded() {
        loaded = true;
    }
    public Boolean getLoaded() {
        return loaded;
    }
    public void setObject(Object o) {
        this.object = o;
        getUser().registerObject(getClassIdentifier(), getObjectIdentifier(), this);
    }
    public ConcurrentHashMap<String, String> getRemoteEdits() {
        return edits;
    }

}
