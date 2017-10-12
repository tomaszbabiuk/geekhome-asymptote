package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public abstract class ActionItemViewModel extends SelectableItemViewModel implements LayoutHolder {
    private final ManageViewModel _manageViewModel;
    private final SensorItemViewModel _sensor;
    private final int _iconId;
    private final int _nameResId;
    private final int _descriptionResId;

    ActionItemViewModel(ManageViewModel manageViewModel, SensorItemViewModel sensor,
                        @RawRes int iconId, @StringRes int nameResId, @StringRes int descriptionResId) {
        _manageViewModel = manageViewModel;
        _sensor = sensor;
        _iconId = iconId;
        _nameResId = nameResId;
        _descriptionResId = descriptionResId;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_action;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public void select() {
        _manageViewModel.selectAction(this);
        setSelected(true);
    }

    public abstract void execute();

    @Bindable
    public int getIconId() {
        return _iconId;
    }

    @Bindable
    public int getNameResId() {
        return _nameResId;
    }

    @Bindable
    public int getDescriptionResId() {
        return _descriptionResId;
    }
}
