package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

public class SelectableItemViewModel extends BaseObservable {
    private boolean _selected;

    @Bindable
    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean selected) {
        _selected = selected;
        notifyPropertyChanged(BR.selected);
    }
}
