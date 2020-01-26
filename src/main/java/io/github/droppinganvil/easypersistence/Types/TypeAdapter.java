package io.github.droppinganvil.easypersistence.Types;


import java.util.Collection;

public class TypeAdapter {
    private Class<?> extender;

    public TypeAdapter(Class<?> extender) {
        this.extender = extender;

    }

    public void process(String fieldName, String fieldValue, Object o) {
        if (o instanceof Collection) {
            processCollection(o);
            return;
        }

    }

    private void processCollection(Object o) {

    }

}
