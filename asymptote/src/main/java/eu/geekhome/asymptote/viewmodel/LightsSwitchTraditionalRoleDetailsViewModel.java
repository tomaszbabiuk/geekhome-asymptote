package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentLightsSwitchTraditionalRoleDetailsBinding;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class LightsSwitchTraditionalRoleDetailsViewModel extends ViewModel<FragmentLightsSwitchTraditionalRoleDetailsBinding> {
    private EditSensorViewModel _parent;
    private SensorItemViewModel _sensor;
    private ObservableArrayList<LayoutHolder> _switchModes;
    private HelpActionBarViewModel _actionBarModel;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;

    public LightsSwitchTraditionalRoleDetailsViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                                       EditSensorViewModel parent, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _switchModes = generateSwitchModes(sensor);
        _parent = parent;
        _sensor = sensor;
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    private ObservableArrayList<LayoutHolder> generateSwitchModes(SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();

        boolean newRole = sensor.getSyncData().getRole() != BoardRole.LIGHT_SWITCH_TRADITIONAL;
        int channels = sensor.getSyncData().getRelayStates().length;
        if (channels > 0) {
            for (int i = 0; i < sensor.getSyncData().getRelayStates().length; i++) {
                long value = newRole ? 0 : sensor.getSyncData().getParams()[i];
                if (value < 0) {
                    value = 0;
                }
                SwitchModeItemViewModel impulseModel =  _factory.createSwitchModeItemViewModel(value, i);
                result.add(impulseModel);
            }
        }

        return result;
    }


    @Override
    public FragmentLightsSwitchTraditionalRoleDetailsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        final FragmentLightsSwitchTraditionalRoleDetailsBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_lights_switch_traditional_role_details, container, false);
        binding.setVm(this);

        return binding;
    }

    public void onDone() {
        getSensor().setBlocked(true);
        getParent().commit();

        for (LayoutHolder holder : _switchModes) {
            SwitchModeItemViewModel model = (SwitchModeItemViewModel) holder;
            paramChanged(model.getChannel(), model.getMode());
        }

        getSensor().requestFullSync();

        _navigationService.goBackTo(MainViewModel.class);
    }

    private void paramChanged(int paramIndex, long paramValue) {
        getSensor().getUpdates().add(new ParamSyncUpdate(new ParamValue(paramIndex, paramValue)));
    }

    public EditSensorViewModel getParent() {
        return _parent;
    }

    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getSwitchModes() {
        return _switchModes;
    }
}
