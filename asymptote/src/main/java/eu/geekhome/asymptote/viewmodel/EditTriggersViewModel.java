package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentEditTriggersBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditTriggersViewModel extends ViewModel<FragmentEditTriggersBinding> {

    private ObservableArrayList<LayoutHolder> _triggers;
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

    public EditTriggersViewModel(MainViewModelsFactory factory, NavigationService navigationService, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _sensor = sensor;
        _triggers = new ObservableArrayList<>();
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    @Override
    public FragmentEditTriggersBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditTriggersBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_triggers, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getTriggers() {
        return _triggers;
    }

    public void addTrigger() {
        ChooseTriggerViewModel viewModel = _factory.createChooseTriggerViewModel(_sensor);
        _navigationService.showOverlayViewModel(viewModel);
    }

}
