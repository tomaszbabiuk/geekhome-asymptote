package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;

public abstract class EditValueViewModelBase<V> extends BaseObservable {
    abstract protected V buildValue();
    abstract public void applyValue(V value);
}
