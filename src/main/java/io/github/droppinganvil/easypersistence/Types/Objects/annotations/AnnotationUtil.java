package io.github.droppinganvil.easypersistence.Types.Objects.annotations;

import java.lang.reflect.Field;

public class AnnotationUtil {
    public static boolean shouldSaveObject(Field field) {
        return !field.isAnnotationPresent(DontSave.class);
    }
}
