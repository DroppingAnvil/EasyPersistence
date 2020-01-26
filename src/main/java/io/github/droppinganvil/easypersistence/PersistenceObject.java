package io.github.droppinganvil.easypersistence;

import io.github.droppinganvil.easypersistence.Types.Format;
import io.github.droppinganvil.easypersistence.Types.YAMLAdapter;

import java.io.File;

public class PersistenceObject extends Identifier {
    private Format format;
    private Object object;
    private File file;
    //Loading a previous OR creating new specific
    public PersistenceObject(String classID, String objectID, PersistenceUser user, Format format, Object object, Boolean quickLoad) {
        super(classID, objectID, user);
        this.format = format;
        this.object = object;
        this.file = new File(new File(super.getUser().getDirectory(), super.getClassIdentifier()), super.getObjectIdentifier());
        user.registerObject(classID, objectID, this);
        if (quickLoad) {
            PersistenceManager.quickLoad.add(this);
        } else PersistenceManager.largeLoad.add(this);
    }
    public Format getFormat() {
        return format;
    }
    public Object getObject() {
        return object;
    }
    public void load() {
        switch (format) {
            case YAML:
                YAMLAdapter.process(this);
                break;
        }
    }

}
