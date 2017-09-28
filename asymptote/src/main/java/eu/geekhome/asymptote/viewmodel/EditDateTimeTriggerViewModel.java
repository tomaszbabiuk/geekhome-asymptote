package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentEditDatetimeTriggerBinding;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class EditDateTimeTriggerViewModel extends ViewModel<FragmentEditDatetimeTriggerBinding> {

    private HelpActionBarViewModel _actionBarModel;
    private final GeneralDialogService _generalDialogService;
    private final SensorItemViewModel _sensor;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;
    private ArrayList<String> _channels;
    private ArrayList<String> _states;
    private boolean _editMode;
    private long _time;
    private long _date;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditDateTimeTriggerViewModel(MainViewModelsFactory factory, NavigationService navigationService,
                                        GeneralDialogService generalDialogService, SensorItemViewModel sensor) {
        _factory = factory;
        _navigationService = navigationService;
        _generalDialogService = generalDialogService;
        _sensor = sensor;
        _actionBarModel = _factory.createHelpActionBarModel();
        _channels = new ArrayList<>();
        _channels.add("Channel 1");
        _channels.add("Channel 2");
        _states = new ArrayList<>();
        _states.add("wł");
        _states.add("wył");
    }

    @Override
    public FragmentEditDatetimeTriggerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentEditDatetimeTriggerBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_datetime_trigger, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public boolean isEditMode() {
        return _editMode;
    }

    public void setEditMode(boolean editMode) {
        _editMode = editMode;
        notifyPropertyChanged(BR.editMode);
    }

    public void done() {
//        Date date = getBinding().picker.getDate();
//        RelayValue value = new RelayValue(0, isOnOffValue());
//        DateTimeTriggerValue<RelayValue> triggerValue = new DateTimeTriggerValue<>(date, value);
//        DateTimeTriggerSyncUpdate<RelayValue> update = new DateTimeTriggerSyncUpdate<>(triggerValue);
//        _sensor.getUpdates().add(update);
//        _sensor.requestFullSync();
//
//        _navigationService.goBack();
    }

    public void onPickTime() {
        _generalDialogService.pickTime((int)getTime(), new GeneralDialogService.TimePickerListener() {
            @Override
            public void onTimePicked(int hourOfDay, int minute, int second) {
                int time = hourOfDay * 3600 + minute * 60 + second;
                setTime(time);
            }
        });
    }

    public void onPickDate() {
        _generalDialogService.pickDate((int)getDate(), new GeneralDialogService.DatePickerListener() {
            @Override
            public void onDataPicked(int year, int month, int day) {
                int date = year * 12 * 31 + month * 31 + day;
                setDate(date);
            }
        });
    }

    @Bindable
    public ArrayList<String> getChannels() {
        return _channels;
    }

    @Bindable
    public ArrayList<String> getStates() {
        return _states;
    }

    @Bindable
    public long getTime() {
        return _time;
    }

    public void setTime(long time) {
        _time = time;
        notifyPropertyChanged(BR.time);
    }

    @Bindable
    public long getDate() {
        return _date;
    }

    public void setDate(long date) {
        _date = date;
        notifyPropertyChanged(BR.date);
    }
}
