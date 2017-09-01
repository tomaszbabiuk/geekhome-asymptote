package eu.geekhome.asymptote.bindingutils;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;

public abstract class InjectedViewModel<T extends ViewDataBinding> extends ViewModel<T> {
    private final ActivityComponent _activityComponent;

    public InjectedViewModel(ActivityComponent activityComponent) {
        _activityComponent = activityComponent;
        doInject(activityComponent);
    }

    protected abstract void doInject(ActivityComponent activityComponent);

    public ActivityComponent getActivityComponent() {
        return _activityComponent;
    }
}
