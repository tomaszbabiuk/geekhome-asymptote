package eu.geekhome.asymptote.model;

public abstract class SyncUpdateBase<T> implements SyncUpdate<T>  {
    private T _value;

    @Override
    public T getValue() {
        return _value;
    }

    public void setValue(T value) {
        _value = value;
    }

    public SyncUpdateBase(T value) {
        _value = value;
    }
}
