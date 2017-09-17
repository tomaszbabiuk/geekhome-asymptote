package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class FirmwareItemViewModel extends SelectableItemViewModel implements LayoutHolder {
    private final SensorItemViewModel _sensor;
    private final String _name;
    private final String _description;
    private final FirmwareItemViewModel.Context _firmwareContext;
    private final Firmware _firmware;
    private final MainViewModelsFactory _factory;
    private final NavigationService _navigationService;

    public FirmwareItemViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                 Firmware firmware, SensorItemViewModel sensor,
                                 String name, String description, FirmwareItemViewModel.Context context) {
        _navigationService = navigationService;
        _factory = factory;
        _firmware = firmware;
        _sensor = sensor;
        _name = name;
        _description = description;
        _firmwareContext = context;
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
        OtaViewModel otaViewModel = _factory.createOtaViewModel(_sensor, _firmware);
        _navigationService.showViewModel(otaViewModel);
    }

    public enum Context {
        Actual,
        Update,
        Change,
    }
}
