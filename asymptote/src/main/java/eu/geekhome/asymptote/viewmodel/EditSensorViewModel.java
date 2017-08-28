package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentEditSensorBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.ColorSyncUpdate;
import eu.geekhome.asymptote.model.NameSyncUpdate;
import eu.geekhome.asymptote.model.RoleSyncUpdate;
import eu.geekhome.asymptote.services.NavigationService;

public class EditSensorViewModel extends ViewModel<FragmentEditSensorBinding> {

    private int _newColor;
    private BoardRole _newRole;
    private ObservableArrayList<LayoutHolder> _roles;
    private HelpActionBarViewModel _actionBarModel;
    private final SensorItemViewModel _sensor;

    @Inject
    NavigationService _navigationService;
    @Inject
    Context _context;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    private String _newName;

    @Bindable
    public String getNewName() {
        return _newName;
    }

    public void setNewName(String value) {
        _newName = value;
    }

    public EditSensorViewModel(ActivityComponent activityComponent, SensorItemViewModel sensor) {
        super(activityComponent);
        _sensor = sensor;
        _roles = RoleCreator.generateRoles(_context, sensor.getSyncData().getDeviceKey().getBoardId(), this);
        _actionBarModel = new HelpActionBarViewModel(activityComponent);

        setNewName(sensor.getSyncData().getName());
        setNewColor(sensor.getSyncData().getColor());
        setNewRole(sensor.getSyncData().getRole());
    }

    @Override
    public FragmentEditSensorBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditSensorBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_sensor, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    public void chooseColor(int color) {
        setNewColor(color);
    }

    @Bindable
    public int getNewColor() {
        return _newColor;
    }

    public void setNewColor(int newColor) {
        _newColor = newColor;
        notifyPropertyChanged(BR.newColor);
    }

    public void onNext() {
        boolean reset = !getNewRole().equals(getSensor().getSyncData().getRole());
        if (getNewRole() == BoardRole.HEATING_THERMOSTAT) {
            String title = _context.getString(R.string.heating_thermostat_details);
            String instruction = _context.getString(R.string.heating_thermostat_instruction);
            XStatRoleDetailsViewModelBase details = new ThermostatRoleDetailsViewModel(
                    getActivityComponent(), this, getSensor(), title, instruction, reset);
            _navigationService.showViewModel(details, new ShowBackButtonInToolbarViewParam());
        } else if (getNewRole() == BoardRole.COOLING_THERMOSTAT) {
            String title = _context.getString(R.string.cooling_thermostat_details);
            String instruction = _context.getString(R.string.cooling_thermostat_instruction);
            XStatRoleDetailsViewModelBase details = new ThermostatRoleDetailsViewModel(
                    getActivityComponent(), this, getSensor(), title, instruction, reset);
            _navigationService.showViewModel(details, new ShowBackButtonInToolbarViewParam());
        } else if (getNewRole() == BoardRole.DRYING_HYGROSTAT) {
            String title = _context.getString(R.string.drying_hygrostat_details);
            String instruction = _context.getString(R.string.drying_hygrostat_instruction);
            XStatRoleDetailsViewModelBase details = new HygrostatRoleDetailsViewModel(
                    getActivityComponent(), this, getSensor(), title, instruction, reset);
            _navigationService.showViewModel(details, new ShowBackButtonInToolbarViewParam());
        } else if (getNewRole() == BoardRole.HUMIDIFICATION_HYGROSTAT) {
            String title = _context.getString(R.string.humidification_hygrostat_details);
            String instruction = _context.getString(R.string.humidification_hygrostat_instruction);
            XStatRoleDetailsViewModelBase details = new HygrostatRoleDetailsViewModel(
                    getActivityComponent(), this, getSensor(), title, instruction, reset);
            _navigationService.showViewModel(details, new ShowBackButtonInToolbarViewParam());
        } else if (getNewRole().isAdvanced()) {
            MainsAdvancedRoleDetailsViewModel model = new MainsAdvancedRoleDetailsViewModel(getActivityComponent(),
                    this, getSensor());
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        } else {
            getSensor().setBlocked(true);
            commit();
            getSensor().onRequestFullSync();
            _navigationService.goBack();
        }
    }

    void commit() {
        if (!getNewName().equals(_sensor.getSyncData().getName())) {
            nameChanged(getNewName());
        }

        if (getNewColor() != _sensor.getSyncData().getColor()) {
            colorChanged(getNewColor());
        }

        if (getNewRole() != _sensor.getSyncData().getRole()) {
            roleChanged(getNewRole());
        }
    }

    @Bindable
    public BoardRole getNewRole() {
        return _newRole;
    }

    public void setNewRole(BoardRole newRole) {
        _newRole = newRole;
        selectRole(newRole);
        notifyPropertyChanged(BR.newRole);
    }

    private void selectRole(BoardRole roleId) {
        if (_roles != null) {
            for (LayoutHolder holderItem : _roles) {
                RoleItemViewModel roleItem = (RoleItemViewModel) holderItem;
                roleItem.setSelected(roleItem.getRole() == roleId);
            }
        }
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getRoles() {
        return _roles;
    }

    public void nameChanged(String newName) {
        _sensor.getUpdates().add(new NameSyncUpdate(newName));
    }

    public void colorChanged(int newColor) {
        _sensor.getUpdates().add(new ColorSyncUpdate(newColor));
    }

    public void roleChanged(BoardRole role) {
        _sensor.getUpdates().add(new RoleSyncUpdate(role));
    }
}
