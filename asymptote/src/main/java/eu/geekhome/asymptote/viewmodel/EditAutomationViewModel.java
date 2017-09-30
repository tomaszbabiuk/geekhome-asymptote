package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentEditAutomationBinding;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.AutomationAddedListener;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditAutomationViewModel extends ViewModel<FragmentEditAutomationBinding> implements AutomationAddedListener {

    private ObservableArrayList<LayoutHolder> _automations;
    private HelpActionBarViewModel _actionBarModel;
    private final SensorItemViewModel _sensor;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditAutomationViewModel(MainViewModelsFactory factory, NavigationService navigationService, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _sensor = sensor;
        _automations = new ObservableArrayList<>();
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    @Override
    public FragmentEditAutomationBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditAutomationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_automation, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getAutomations() {
        return _automations;
    }

    public void addTrigger() {
        int index = findFirstFreeIndex();
        ChooseTriggerViewModel viewModel = _factory.createChooseTriggerViewModel(this, index, _sensor);
        _navigationService.showOverlayViewModel(viewModel);
    }

    private int findFirstFreeIndex() {
        return 0;
    }

    @Override
    public void onAutomationAdded(Automation automation) {
        AutomationItemViewModel automationModel = new AutomationItemViewModel(automation, this);
        _automations.add(automationModel);
    }

    @Override
    public void onAutomationEdit(Automation automation) {
        AutomationItemViewModel toDelete = null;
        for (LayoutHolder a : _automations) {
            if (a instanceof AutomationItemViewModel && ((AutomationItemViewModel) a).getAutomation().getIndex() == automation.getIndex()) {
                toDelete = (AutomationItemViewModel) a;
                break;
            }
        }

        if (toDelete != null) {
            _automations.remove(toDelete);
        }

        AutomationItemViewModel automationModel = new AutomationItemViewModel(automation, this);
        _automations.add(automationModel);
    }
    void deleteAutomation(AutomationItemViewModel toDelete) {
        _automations.remove(toDelete);
    }

    void editAutomation(AutomationItemViewModel toEdit) {
        if (toEdit.getAutomation() instanceof AutomationDateTimeRelay) {
            AutomationDateTimeRelay automation = (AutomationDateTimeRelay)toEdit.getAutomation();
            EditAutomationDateTimeRelayValueViewModel model = _factory.createEditDateTimeTriggerViewModel(this, _sensor, automation);
            _navigationService.showViewModel(model, new ShowBackButtonInToolbarViewParam());
        }
    }
}
