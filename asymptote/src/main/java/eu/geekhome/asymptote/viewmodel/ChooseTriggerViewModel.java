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
import eu.geekhome.asymptote.databinding.DialogChooseTriggerBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.utils.KeyboardHelper;

public class ChooseTriggerViewModel extends ViewModel<DialogChooseTriggerBinding> {
    private final NavigationService _navigationService;

    private TriggerType _selectedTriggerType;
    private ObservableArrayList<LayoutHolder> _triggers;


    public ChooseTriggerViewModel(NavigationService navigationService, SensorItemViewModel sensor) {
        _navigationService = navigationService;
        _triggers = createTriggers(sensor);
    }

    private ObservableArrayList<LayoutHolder> createTriggers(SensorItemViewModel sensor) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();
        TriggerItemViewModel exactTrigger = new TriggerItemViewModel(this, sensor, TriggerType.ExactTime);
        exactTrigger.setSelected(true);
        TriggerItemViewModel scheduleTrigger = new TriggerItemViewModel(this, sensor, TriggerType.Scheduler);
        result.add(exactTrigger);
        result.add(scheduleTrigger);
        return result;
    }

    @Override
    public DialogChooseTriggerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogChooseTriggerBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.dialog_choose_trigger, container, false);
        binding.setVm(this);
        return binding;
    }

    public void close() {
        KeyboardHelper.hideKeyboard(getBinding().getRoot());
        _navigationService.goBack();
    }

    public void ok() {
    }

    @Bindable
    public void setSelectedTriggerType(TriggerType triggerType) {
        _selectedTriggerType = triggerType;
        notifyPropertyChanged(BR.selectedTriggerType);
    }

    @Bindable
    public TriggerType getSelectedTriggerType() {
        return _selectedTriggerType;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getTriggers() {
        return _triggers;
    }

    public void selectTrigger(TriggerType triggerType) {
        if (_triggers != null) {
            for (LayoutHolder holderItem : _triggers) {
                TriggerItemViewModel roleItem = (TriggerItemViewModel) holderItem;
                roleItem.setSelected(roleItem.getTriggerType() == triggerType);
            }
        }
    }
}
