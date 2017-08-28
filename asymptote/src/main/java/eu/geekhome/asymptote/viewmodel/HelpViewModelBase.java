package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;

public abstract class HelpViewModelBase<T extends ViewDataBinding> extends WiFiAwareViewModel<T> {
    private HelpActionBarViewModel _actionBarModel;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    public HelpViewModelBase(ActivityComponent activityComponent) {
        super(activityComponent);
        _actionBarModel = new HelpActionBarViewModel(activityComponent);
    }

    @Override
    protected boolean isCloudOnlyAllowed() {
        return false;
    }
}