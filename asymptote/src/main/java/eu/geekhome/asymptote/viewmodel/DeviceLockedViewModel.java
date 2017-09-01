package eu.geekhome.asymptote.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.InjectedViewModel;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.DialogDeviceLockedBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;

public class DeviceLockedViewModel extends InjectedViewModel<DialogDeviceLockedBinding> {
    @Inject
    NavigationService _navigationService;

    public DeviceLockedViewModel(ActivityComponent activityComponent) {
        super(activityComponent);
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {

    }

    @Override
    public DialogDeviceLockedBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogDeviceLockedBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_device_locked, container, false);
        binding.setVm(this);
        return binding;
    }

    public void close() {
        _navigationService.goBack();
    }
}
