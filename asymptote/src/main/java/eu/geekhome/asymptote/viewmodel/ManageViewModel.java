package eu.geekhome.asymptote.viewmodel;

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
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class ManageViewModel extends ViewModel<FragmentManageBinding> {
    private final MainViewModelsFactory _factory;
    private final NavigationService _navigationService;
    private HelpActionBarViewModel _actionBarModel;
    private ObservableArrayList<LayoutHolder> _actions;
    private ActionItemViewModel _selectedAction;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }


    public ManageViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                           SensorItemViewModel sensor) {
        _factory = factory;
        _actionBarModel = _factory.createHelpActionBarModel();
        _navigationService = navigationService;
        _actions = createActions(sensor);
    }

    private ObservableArrayList<LayoutHolder> createActions(final SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();

        result.add(new ActionItemViewModel(this, sensor, R.raw.gear, R.string.settings, R.string.change_name_color_role) {
            @Override
            public void execute() {
                EditSensorViewModel model = _factory.createEditSensorViewModel(sensor);
                _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
            }
        });

        result.add(new ActionItemViewModel(this, sensor, R.raw.robot, R.string.automation, R.string.define_automation) {
            @Override
            public void execute() {
                EditAutomationViewModel model = _factory.createEditAutomationViewModel(sensor);
                _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
            }
        });

        result.add(new ActionItemViewModel(this, sensor, R.raw.microchip, R.string.firmware, R.string.change_firmware) {
            @Override
            public void execute() {
                ChangeFirmwareViewModel model = _factory.createChangeFirmwareViewModel(sensor);
                _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
            }
        });

        if (sensor.getSyncData().getSystemInfo().getVariant().isWifi()) {
            if (sensor.getSyncData().isLocked()) {
                result.add(new ActionItemViewModel(this, sensor, R.raw.unlocked, R.string.unlock, R.string.unlock) {
                    @Override
                    public void execute() {
                        sensor.unlock();
                    }
                });
            } else {
                result.add(new ActionItemViewModel(this, sensor, R.raw.locked, R.string.lock, R.string.lock) {
                    @Override
                    public void execute() {
                        sensor.lock();
                    }
                });
            }
        }

        selectAction((ActionItemViewModel)result.get(0));

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
        _navigationService.goBack();
        getSelectedAction().execute();
    }

    @Bindable
    public void setSelectedAction(ActionItemViewModel action) {
        _selectedAction = action;
        notifyPropertyChanged(BR.selectedAction);
    }

    @Bindable
    public ActionItemViewModel getSelectedAction() {
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
