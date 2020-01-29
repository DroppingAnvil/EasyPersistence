package io.github.droppinganvil.easypersistence;

import java.io.File;

public class PersistenceObject extends Identifier {
    private Object object;
    private Integer maxCycles;
    private Integer cycles;
    private File file;
    private Boolean loaded = false;
    //Loading a previous OR creating new specific
    public PersistenceObject(String classID, String objectID, PersistenceUser user, Object object) {
        super(classID, objectID, user);
        this.object = object;
        this.file = new File(new File(super.getUser().getDirectory(), super.getClassIdentifier()), super.getObjectIdentifier());
        maxCycles = 3;
        cycles = 3;
        user.registerObject(classID, objectID, this);
    }
    public PersistenceObject(String classID, String objectID, PersistenceUser user, Object object, Integer maxCycles, Integer cycles) {
        super(classID, objectID, user);
        this.object = object;
        this.file = new File(new File(super.getUser().getDirectory(), super.getClassIdentifier()), super.getObjectIdentifier());
        this.maxCycles = maxCycles;
        this.cycles = cycles;
        user.registerObject(classID, objectID, this);
    }
    public PersistenceObject(String classID, String objectID, PersistenceUser user, Object object, Integer maxCycles, Integer cycles, File file) {
        super(classID, objectID, user);
        this.object = object;
        this.file = file;
        this.maxCycles = maxCycles;
        this.cycles = cycles;
        user.registerObject(classID, objectID, this);
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

}
