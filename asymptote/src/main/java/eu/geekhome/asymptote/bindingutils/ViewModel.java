package eu.geekhome.asymptote.bindingutils;

import android.databinding.BaseObservable;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;

public abstract class ViewModel<T extends ViewDataBinding> extends BaseObservable {
    private final ActivityComponent _activityComponent;
    private T _binding;

    public abstract T createBinding(LayoutInflater inflater, ViewGroup container);

    public ViewModel(ActivityComponent activityComponent) {
        _activityComponent = activityComponent;
        doInject(activityComponent);
    }

//    public ViewModel() {
//        DaggerApplicationComponent.builder().
//                .baseComponent(PSAApplication.getBaseComponent())
//                .build()
//                .inject(utilityWrapper);
//    }

    protected abstract void doInject(ActivityComponent activityComponent);

    public BoundFragment createFragment(ViewParam... params) {
        BoundFragment fragment = new BoundFragment();
        fragment.setModel(this);
        fragment.setViewParams(params);
        return fragment;
    }


    public void onResume() {
    }

    public void onPause() {
    }

    public boolean goingBack() {
        return true;
    }

    public void setBinding(T binding) {
        _binding = binding;
    }

    public T getBinding() {
        return _binding;
    }

    public ActivityComponent getActivityComponent() {
        return _activityComponent;
    }
}
