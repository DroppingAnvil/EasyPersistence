package io.github.droppinganvil.easypersistence.Panel.Actions;

public interface Action {
    String getAction();
    String parse(String data);

}
