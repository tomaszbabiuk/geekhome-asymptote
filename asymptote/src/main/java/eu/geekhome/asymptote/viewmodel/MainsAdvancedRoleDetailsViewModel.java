package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.InjectedViewModel;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentMainsAdvRoleDetailsBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.ParamSyncUpdate;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.services.NavigationService;

public class MainsAdvancedRoleDetailsViewModel extends InjectedViewModel<FragmentMainsAdvRoleDetailsBinding> {
    private final ObservableArrayList<LayoutHolder> _interlocks;
    private EditSensorViewModel _parent;
    private SensorItemViewModel _sensor;
    private ObservableArrayList<LayoutHolder> _workingModes;
    private HelpActionBarViewModel _actionBarModel;

    @Inject
    NavigationService _navigationService;

    public MainsAdvancedRoleDetailsViewModel(ActivityComponent activityComponent, EditSensorViewModel parent,
                                             SensorItemViewModel sensor) {
        super(activityComponent);
        _workingModes = generateWorkingModes(sensor);
        _interlocks = generateInterlocks(sensor);
        _parent = parent;
        _sensor = sensor;
        _actionBarModel = new HelpActionBarViewModel(getActivityComponent());
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    private ObservableArrayList<LayoutHolder> generateWorkingModes(SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();

        boolean newRole = !sensor.getSyncData().getRole().isAdvanced();
        int channels = sensor.getSyncData().getRelayStates().length;
        if (channels > 0) {
            for (int i = 0; i < sensor.getSyncData().getRelayStates().length; i++) {
                long value = newRole ? 0 : sensor.getSyncData().getParams()[i * 2];
                RelayWorkingModeItemViewModel impulseModel = new RelayWorkingModeItemViewModel(getActivityComponent(), value, i);
                result.add(impulseModel);
            }
        }

        return result;
    }

    private ObservableArrayList<LayoutHolder> generateInterlocks(SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();

        boolean newRole = !sensor.getSyncData().getRole().isAdvanced();
        int channels = sensor.getSyncData().getRelayStates().length;
        if (channels > 0) {
            for (int i = 0; i < sensor.getSyncData().getRelayStates().length; i++) {
                long lockGroup = newRole ? 0 : sensor.getSyncData().getParams()[i * 2 + 1];
                RelayInterlockItemViewModel impulseModel = new RelayInterlockItemViewModel(i, lockGroup, channels);
                result.add(impulseModel);
            }
        }

        return result;
    }

    @Override
    public FragmentMainsAdvRoleDetailsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        final FragmentMainsAdvRoleDetailsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mains_adv_role_details, container, false);
        binding.setVm(this);

        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void onDone() {
        getSensor().setBlocked(true);
        getParent().commit();

        for (LayoutHolder holder : _workingModes) {
            RelayWorkingModeItemViewModel model = (RelayWorkingModeItemViewModel) holder;
            paramChanged(model.getChannel() * 2, model.getImpulse());
        }

        for (LayoutHolder holder : _interlocks) {
            RelayInterlockItemViewModel model = (RelayInterlockItemViewModel) holder;
            paramChanged(model.getChannel() * 2 + 1, model.getLockGroup());
        }

        getSensor().onRequestFullSync();

        _navigationService.goBackTo(MainViewModel.class);
    }

    public void paramChanged(int paramIndex, long paramValue) {
        getSensor().getUpdates().add(new ParamSyncUpdate(new ParamValue(paramIndex, paramValue)));
    }

    public EditSensorViewModel getParent() {
        return _parent;
    }

    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getWorkingModes() {
        return _workingModes;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getInterlocks() {
        return _interlocks;
    }
}
