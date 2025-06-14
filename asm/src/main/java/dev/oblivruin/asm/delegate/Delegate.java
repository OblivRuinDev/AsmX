package dev.oblivruin.asm.delegate;

public abstract class Delegate<T> {
    public final T parent;

    protected Delegate(T parent) {
        if (parent == null) {
            throw new NullPointerException();
        }
        this.parent = parent;
    }
}
