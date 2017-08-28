package eu.geekhome.asymptote.model;

public interface SyncUpdate<T> {
    T getValue();
    void setValue(T value);
    boolean isBlocking();
}
