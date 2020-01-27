package io.github.droppinganvil.easypersistence.Types.Objects.Response;

import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.Error;
import io.github.droppinganvil.easypersistence.Notifications.ErrorHandling.ErrorType;
import io.github.droppinganvil.easypersistence.Types.Objects.Buildable;
import io.github.droppinganvil.easypersistence.Types.Objects.ComplexBuildable;

public class LoadedObject {
    private Object object;
    private Object builder;
    private Boolean complex = false;

    public LoadedObject(Object object, Object builder) {
        this.object = object;
        if (this.object == null) {
            new Error(ErrorType.Null_Object).addObject(this).complete().send();
        }
        this.builder = builder;
        if (this.builder == null || !(this.builder instanceof Buildable || this.builder instanceof ComplexBuildable)) {
            new Error(ErrorType.Issue_Generic).addObject(this).complete().send();
        }
        if (this.builder instanceof ComplexBuildable) complex = true;
    }

    public Object getObject() {
        return object;
    }

    public Object getBuilder() {
        return builder;
    }

    public Boolean getComplex() {
        return complex;
    }
}
