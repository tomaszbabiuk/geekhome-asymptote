package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.net.InetAddress;
import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeHumidity;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.AutomationDateTimeTemperature;
import eu.geekhome.asymptote.model.AutomationSchedulerHumidity;
import eu.geekhome.asymptote.model.AutomationSchedulerRelay;
import eu.geekhome.asymptote.model.AutomationSchedulerTemperature;
import eu.geekhome.asymptote.model.AutomationSyncUpdate;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.DeleteAutomationSyncUpdate;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.SyncListener;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationViewModel extends ViewModel<FragmentEditAutomationBinding> implements AutomationAddedListener, SyncListener {

    private ObservableArrayList<LayoutHolder> _automationList;
    private HelpActionBarViewModel _actionBarModel;
    private final SyncManager _syncManager;
    private final ThreadRunner _threadRunner;
    private final CloudDeviceService _cloudDeviceService;
    private final SensorItemViewModel _sensor;
    private final Context _context;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;
    private boolean _loadingAutomationList;
    private boolean _savingAutomationList;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditAutomationViewModel(MainViewModelsFactory factory, Context context, NavigationService navigationService,
                                   SyncManager syncManager, ThreadRunner threadRunner, CloudDeviceService cloudDeviceService,
                                   SensorItemViewModel sensor) {
        _factory = factory;
        _context = context;
        _navigationService = navigationService;
        _syncManager = syncManager;
        _threadRunner = threadRunner;
        _cloudDeviceService = cloudDeviceService;
        _sensor = sensor;
        _automationList = new ObservableArrayList<>();
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (_sensor.isHasWiFiSignal()) {
            if (_automationList.size() == 0) {
                _syncManager.setSyncListener(this);
                _sensor.requestAutomationList();
            }
        } else {
            _cloudDeviceService.setSyncListener(this);
            _cloudDeviceService.registerForAutomationSyncEvents(_sensor.getUserId(), _sensor.getToken());
        }

        if (_automationList.size() == 0) {
            setLoadingAutomationList(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        _syncManager.setSyncListener(null);
        _cloudDeviceService.setSyncListener(null);
        _cloudDeviceService.unregisterFromAutomationSyncEvents(_sensor.getUserId());
    }

    @Override
    public FragmentEditAutomationBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getAutomationList() {
        return _automationList;
    }

    public void addTrigger() {
        int index = findFirstFreeIndex();
        ChooseAutomationViewModel viewModel = _factory.createChooseTriggerViewModel(this, index, _sensor);
        _navigationService.showOverlayViewModel(viewModel);
    }

    private int findFirstFreeIndex() {
        int i=0;

        boolean indexTaken;
        do {
            indexTaken = false;
            for (LayoutHolder automationHolder : _automationList) {
                Automation automation = ((AutomationItemViewModel) automationHolder).getAutomation();
                if (automation.getIndex() == i) {
                    indexTaken = true;
                    i++;
                    break;
                }
            }
        } while (indexTaken);

        return i;
    }

    @Override
    public void onAutomationAdded(Automation automation) {
        AutomationItemViewModel automationModel = new AutomationItemViewModel(_context, automation, this);
        _automationList.add(automationModel);
    }

    @Override
    public void onAutomationEdit(Automation automation) {
        AutomationItemViewModel toDelete = null;
        for (LayoutHolder a : _automationList) {
            if (a instanceof AutomationItemViewModel && ((AutomationItemViewModel) a).getAutomation().getIndex() == automation.getIndex()) {
                toDelete = (AutomationItemViewModel) a;
                break;
            }
        }

        if (toDelete != null) {
            _automationList.remove(toDelete);
        }

        AutomationItemViewModel automationModel = new AutomationItemViewModel(_context, automation, this);
        _automationList.add(automationModel);
    }

    void editAutomation(AutomationItemViewModel toEdit) {
        if (toEdit.getAutomation() instanceof AutomationDateTimeRelay) {
            AutomationDateTimeRelay automation = (AutomationDateTimeRelay)toEdit.getAutomation();
            EditAutomationDateTimeRelayViewModel model = _factory.createEditAutomationDateTimeRelayViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        } else if (toEdit.getAutomation() instanceof AutomationDateTimeTemperature) {
            AutomationDateTimeTemperature automation = (AutomationDateTimeTemperature)toEdit.getAutomation();
            EditAutomationDateTimeTemperatureViewModel model = _factory.createEditAutomationDateTimeTemperatureViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        } else if (toEdit.getAutomation() instanceof AutomationDateTimeHumidity) {
            AutomationDateTimeHumidity automation = (AutomationDateTimeHumidity) toEdit.getAutomation();
            EditAutomationDateTimeHumidityViewModel model = _factory.createEditAutomationDateTimeHumidityViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        } else if (toEdit.getAutomation() instanceof AutomationSchedulerRelay) {
            AutomationSchedulerRelay automation = (AutomationSchedulerRelay)toEdit.getAutomation();
            EditAutomationSchedulerRelayViewModel model = _factory.createEditAutomationSchedulerRelayViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        } else if (toEdit.getAutomation() instanceof AutomationSchedulerTemperature) {
            AutomationSchedulerTemperature automation = (AutomationSchedulerTemperature) toEdit.getAutomation();
            EditAutomationSchedulerTemperatureViewModel model = _factory.createEditAutomationSchedulerTemperatureViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        } else if (toEdit.getAutomation() instanceof AutomationSchedulerHumidity) {
            AutomationSchedulerHumidity automation = (AutomationSchedulerHumidity) toEdit.getAutomation();
            EditAutomationSchedulerHumidityViewModel model = _factory.createEditAutomationSchedulerHumidityViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        }
    }

    public void save() {
        _sensor.getUpdates().clear();
        for (LayoutHolder automationHolder : _automationList) {
            AutomationItemViewModel automationViewModel = (AutomationItemViewModel)automationHolder;
            Automation automation = automationViewModel.getAutomation();

            if (automationViewModel.isRemove()) {
                DeleteAutomationSyncUpdate deleteUpdate = new DeleteAutomationSyncUpdate(automation);
                _sensor.getUpdates().add(deleteUpdate);
            } else {
                AutomationSyncUpdate automationUpdate = new AutomationSyncUpdate(automation);
                _sensor.getUpdates().add(automationUpdate);
            }
        }

        _sensor.requestFullSync();
        if (_sensor.isHasWiFiSignal()) {
            _navigationService.goBack();
        } else {
            setSavingAutomationList(true);
        }
    }

    @Override
    public void onAfterSync(InetAddress from, DeviceSyncData syncData, long timestamp, String token) {

    }

    @Override
    public void onUnsupportedBoard(InetAddress from, String deviceId) {

    }

    @Override
    public void onSecuredDeviceFound(InetAddress from) {

    }

    @Override
    public void onCloudDeviceFound(InetAddress from, Variant variant, BoardId boardId, Byte[] restoreTokenPart) {

    }

    @Override
    public void onAutomationListLoaded(final ArrayList<Automation> automationList) {
        setLoadingAutomationList(false);
        if (isSavingAutomationList()) {
            setSavingAutomationList(false);
            _navigationService.goBack();
        } else if (_automationList.size() == 0) {
            _automationList.clear();
            _threadRunner.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (Automation automation : automationList) {
                        AutomationItemViewModel automationModel = new AutomationItemViewModel(_context, automation, EditAutomationViewModel.this);
                        _automationList.add(automationModel);
                    }
                }
            });
        }
    }

    @Bindable
    public boolean isLoadingAutomationList() {
        return _loadingAutomationList;
    }

    public void setLoadingAutomationList(boolean loadingAutomationList) {
        _loadingAutomationList = loadingAutomationList;
        notifyPropertyChanged(BR.loadingAutomationList);
    }

    @Bindable
    public boolean isSavingAutomationList() {
        return _savingAutomationList;
    }

    public void setSavingAutomationList(boolean savingAutomationList) {
        _savingAutomationList = savingAutomationList;
        notifyPropertyChanged(BR.savingAutomationList);
        for (LayoutHolder holder : _automationList) {
            AutomationItemViewModel automationModel = (AutomationItemViewModel)holder;
            automationModel.setLoading(true);
        }
    }
}
