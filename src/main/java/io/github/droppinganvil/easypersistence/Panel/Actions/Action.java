package io.github.droppinganvil.easypersistence.Panel.Actions;

public interface Action {
    //Not using operation so that its possible for actions to be added without modifying Easy Persistence src
    String getAction();
    String parse(String data);

}
