package eu.geekhome.asymptote.viewmodel;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.DialogDeviceLockedBinding;
import eu.geekhome.asymptote.services.NavigationService;

public class DeviceLockedViewModel extends ViewModel<DialogDeviceLockedBinding> {
    private final NavigationService _navigationService;

    public DeviceLockedViewModel(NavigationService navigationService) {
        _navigationService = navigationService;
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
