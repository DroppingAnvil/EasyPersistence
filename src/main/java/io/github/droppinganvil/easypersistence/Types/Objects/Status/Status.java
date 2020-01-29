package io.github.droppinganvil.easypersistence.Types.Objects.Status;

public class Status {
    private State state;
    private String s;

    public Status(State state, String s) {
        this.state = state;
        this.s = s;
    }

    public State getState() {
        return state;
    }
    public String getString() {
        return s;
    }
}
