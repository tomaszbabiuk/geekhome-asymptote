package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;


import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentManageBinding;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class ManageViewModel extends ViewModel<FragmentManageBinding> {
    private final MainViewModelsFactory _factory;
    private final Context _context;
    private final EmergencyManager _emergencyManager;
    private final SyncManager _syncManager;
    private final NavigationService _navigationService;
    private final SensorItemViewModel _sensor;
    private HelpActionBarViewModel _actionBarModel;
    private ObservableArrayList<LayoutHolder> _actions;
    private ActionItemViewModel _selectedAction;
    private String _errorMessage;
    private ToastService _toastService;
    private final ThreadRunner _threadRunner;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
        notifyPropertyChanged(eu.geekhome.asymptote.BR.errorMessage);
    }

    public ManageViewModel(MainViewModelsFactory factory, Context context, NavigationService navigationService,
                           EmergencyManager emergencyManager, SyncManager syncManager,
                           ToastService toastService, ThreadRunner threadRunner, SensorItemViewModel sensor) {
        _factory = factory;
        _context = context;
        _emergencyManager = emergencyManager;
        _syncManager = syncManager;
        _toastService = toastService;
        _threadRunner = threadRunner;
        _actionBarModel = _factory.createHelpActionBarModel();
        _navigationService = navigationService;
        _actions = createActions(sensor);
        _sensor = sensor;
    }

    private ActionItemViewModel createSettingsItem(final SensorItemViewModel sensor) {
        return new ActionItemViewModel(this, sensor, R.raw.gear, R.string.settings, R.string.change_name_color_role) {
            @Override
            public void execute() {
                EditSensorViewModel model = _factory.createEditSensorViewModel(sensor);
                _navigationService.goBack();
                _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
            }
        };
    }

    private ActionItemViewModel createAutomationItem(final SensorItemViewModel sensor) {
        return new ActionItemViewModel(this, sensor, R.raw.robot, R.string.automation_beta, R.string.define_automation) {
            @Override
            public void execute() {
                EditAutomationViewModel model = _factory.createEditAutomationViewModel(sensor);
                _navigationService.goBack();
                _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
            }
        };
    }

    private ActionItemViewModel createFirmwareItem(final SensorItemViewModel sensor) {
        return new ActionItemViewModel(this, sensor, R.raw.microchip, R.string.firmware, R.string.change_firmware) {
            @Override
            public void execute() {
                if (!sensor.getSyncData().isLocked()) {
                    _toastService.makeToast(_context.getString(R.string.device_locked_not_upgradable), true);
                } else {
                    ChangeFirmwareViewModel model = _factory.createChangeFirmwareViewModel(sensor);
                    _navigationService.goBack();
                    _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
                }
            }
        };
    }

    private ActionItemViewModel createLockItem(final SensorItemViewModel sensor) {
        return new ActionItemViewModel(this, sensor, R.raw.locked, R.string.lock, R.string.set_lan_password_to_protect) {
            @Override
            public void execute() {
                if (_emergencyManager.getPassword() == null) {
                    LockViewModel lockViewModel = _factory.createLockViewModel(_sensor);
                    _navigationService.goBack();
                    _navigationService.showViewModel(lockViewModel, new ShowBackButtonInToolbarViewParam());
                } else {
                    _syncManager.lock(_sensor.getSyncData().getSystemInfo().getVariant(), _sensor.getAddress(), new SyncManager.SyncCallback() {
                        @Override
                        public void success() {
                            _sensor.getSyncData().setLocked(true);
                            _sensor.requestSyncDelayed();
                            _navigationService.goBack();
                        }

                        @Override
                        public void failure(Exception exception) {
                            ManageViewModel.this.setErrorMessage(exception.getLocalizedMessage());
                        }
                    }, _emergencyManager.getPassword());
                }
            }
        };
    }

    private ActionItemViewModel createUnlockItem(final SensorItemViewModel sensor) {
        return new ActionItemViewModel(this, sensor, R.raw.unlocked, R.string.unlock, R.string.unlock) {
            @Override
            public void execute() {
                String description = _context.getString(R.string.device_locked);
                ObservableArrayList<LayoutHolder> sections = new ObservableArrayList<>();
                sections.add(new HeaderItemViewModel(HeaderItemViewModel.Style.Main, description));

                String sub = _context.getString(R.string.unlock_note);
                sections.add(new HeaderItemViewModel(HeaderItemViewModel.Style.Sub, sub));

                String[] itemsContent = _context.getResources().getStringArray(R.array.reset_procedure);

                for (int i = 0; i < itemsContent.length; i++) {
                    sections.add(new OrderedItemViewModel(i + 1, itemsContent[i]));
                }

                CMSViewModel model = _factory.createCmsViewModel(sections);
                _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
            }
        };
    }

    private ObservableArrayList<LayoutHolder> createActions(final SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();

        ActionItemViewModel settingsItem = createSettingsItem(sensor);
        result.add(settingsItem);

        ActionItemViewModel automationItem = createAutomationItem(sensor);
        automationItem.setEnabled(sensor.getSyncData().getSystemInfo().getVersionMajor() * 256 + sensor.getSyncData().getSystemInfo().getVersionMinor() == 256 + 6);
        result.add(automationItem);

        ActionItemViewModel firmwareItem = createFirmwareItem(sensor);
        result.add(firmwareItem);

        if (sensor.getSyncData().getSystemInfo().getVariant().isWifi()) {
            if (sensor.getSyncData().isLocked()) {
                ActionItemViewModel unlockItem = createUnlockItem(sensor);
                result.add(unlockItem);
            } else {
                ActionItemViewModel lockItem = createLockItem(sensor);
                result.add(lockItem);
            }
        }

        selectAction((ActionItemViewModel) result.get(0));

        return result;
    }

    @Override
    public FragmentManageBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentManageBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_manage, container, false);
        binding.setVm(this);
        return binding;
    }

    public void onNext() {
        _threadRunner.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getSelectedAction().execute();
            }
        });
    }

    @Bindable
    private void setSelectedAction(ActionItemViewModel action) {
        _selectedAction = action;
        notifyPropertyChanged(BR.selectedAction);
    }

    @Bindable
    private ActionItemViewModel getSelectedAction() {
        return _selectedAction;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getActions() {
        return _actions;
    }

    void selectAction(ActionItemViewModel action) {
        if (_actions != null) {
            for (LayoutHolder holderItem : _actions) {
                SelectableItemViewModel item = (SelectableItemViewModel) holderItem;
                item.setSelected(item.equals(action));
            }
        }
        action.setSelected(true);
        setSelectedAction(action);
    }
}
