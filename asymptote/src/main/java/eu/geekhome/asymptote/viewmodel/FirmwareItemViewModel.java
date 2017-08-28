package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.services.NavigationService;

public class FirmwareItemViewModel extends SelectableItemViewModel implements LayoutHolder {
    private final SensorItemViewModel _sensor;
    private final String _name;
    private final String _description;
    private final FirmwareItemViewModel.Context _firmwareContext;
    private final boolean _available;
    private final Firmware _firmware;
    private final ActivityComponent _activityComponent;

    @Inject
    NavigationService _navigationService;

    public FirmwareItemViewModel(ActivityComponent activityComponent, Firmware firmware, SensorItemViewModel sensor,
                                 String name, String description, FirmwareItemViewModel.Context context,
                                 boolean available) {
        activityComponent.inject(this);
        _activityComponent = activityComponent;
        _firmware = firmware;
        _sensor = sensor;
        _name = name;
        _description = description;
        _firmwareContext = context;
        _available = available;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_firmware;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

    }

    @Bindable
    public Firmware getFirmware() {
        return _firmware;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    @Bindable
    public String getName() {
        return _name;
    }

    @Bindable
    public String getDescription() {
        return _description;
    }

    @Bindable
    public FirmwareItemViewModel.Context getContext() {
        return _firmwareContext;
    }

    public void change() {
        OtaViewModel otaViewModel = new OtaViewModel(_activityComponent, _sensor, _firmware);
        _navigationService.showViewModel(otaViewModel);
    }

    @Bindable
    public boolean isAvailable() {
        return _available;
    }

    public enum Context {
        Actual,
        Update,
        Change,
    }
}
