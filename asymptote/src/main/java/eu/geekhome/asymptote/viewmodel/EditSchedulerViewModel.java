package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Calendar;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.ControlEditSchedulerBinding;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.GeneralDialogService;

public class EditSchedulerViewModel extends ViewModel<ControlEditSchedulerBinding> {
    private final GeneralDialogService _generalDialogService;
    private final SensorItemViewModel _sensor;
    private long _time;
    private boolean _mondaySelected;
    private boolean _tuesdaySelected;
    private boolean _wednesdaySelected;
    private boolean _thursdaySelected;
    private boolean _fridaySelected;
    private boolean _saturdaySelected;
    private boolean _sundaySelected;

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditSchedulerViewModel(GeneralDialogService generalDialogService,
                                  SensorItemViewModel sensor) {
        _generalDialogService = generalDialogService;
        _sensor = sensor;
        Calendar now = Calendar.getInstance();
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        int time = seconds + minutes * 60 + hours * 3600;
        setTime(time);

    }

    @Override
    public ControlEditSchedulerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        ControlEditSchedulerBinding binding = DataBindingUtil.inflate(inflater, R.layout.control_edit_scheduler, container, false);
        binding.setVm(this);
        return binding;
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

    @Bindable
    public long getTime() {
        return _time;
    }

    public void setTime(long time) {
        _time = time;
        notifyPropertyChanged(BR.time);
    }


    SchedulerTrigger buildSchedulerTrigger() {
        int days = 0;
        if (isMondaySelected()) {
            days |= 1 << 1;
        }
        if (isTuesdaySelected()) {
            days |= 1 << 2;
        }
        if (isWednesdaySelected()) {
            days |= 1 << 3;
        }
        if (isThursdaySelected()) {
            days |= 1 << 4;
        }
        if (isFridaySelected()) {
            days |= 1 << 5;
        }
        if (isSaturdaySelected()) {
            days |= 1 << 6;
        }
        if (isSundaySelected()) {
            days |= 1 << 7;
        }
        return new SchedulerTrigger(days, getTime());
    }

    void applySchedulerTrigger(SchedulerTrigger trigger) {
        setDays(trigger.getDays());
        setTime(trigger.getTimeMark());
    }


    private void setDays(int days) {
        setMondaySelected(getBit(days, 1));
        setTuesdaySelected(getBit(days, 2));
        setWednesdaySelected(getBit(days, 3));
        setThursdaySelected(getBit(days, 4));
        setFridaySelected(getBit(days, 5));
        setSaturdaySelected(getBit(days, 6));
        setSundaySelected(getBit(days, 7));
    }

    private boolean getBit(int toGetBitsFrom, int position) {
        return ((toGetBitsFrom >> position) & 1) == 1;
    }

    @Bindable
    public boolean isMondaySelected() {
        return _mondaySelected;
    }

    private void setMondaySelected(boolean mondaySelected) {
        _mondaySelected = mondaySelected;
        notifyPropertyChanged(BR.mondaySelected);
    }

    @Bindable
    public boolean isTuesdaySelected() {
        return _tuesdaySelected;
    }

    private void setTuesdaySelected(boolean tuesdaySelected) {
        _tuesdaySelected = tuesdaySelected;
        notifyPropertyChanged(BR.tuesdaySelected);
    }

    @Bindable
    public boolean isWednesdaySelected() {
        return _wednesdaySelected;
    }

    private void setWednesdaySelected(boolean wednesdaySelected) {
        _wednesdaySelected = wednesdaySelected;
        notifyPropertyChanged(BR.wednesdaySelected);
    }

    @Bindable
    public boolean isThursdaySelected() {
        return _thursdaySelected;
    }

    private void setThursdaySelected(boolean thursdaySelected) {
        _thursdaySelected = thursdaySelected;
        notifyPropertyChanged(BR.thursdaySelected);
    }

    @Bindable
    public boolean isFridaySelected() {
        return _fridaySelected;
    }

    private void setFridaySelected(boolean fridaySelected) {
        _fridaySelected = fridaySelected;
        notifyPropertyChanged(BR.fridaySelected);
    }

    @Bindable
    public boolean isSaturdaySelected() {
        return _saturdaySelected;
    }

    private void setSaturdaySelected(boolean saturdaySelected) {
        _saturdaySelected = saturdaySelected;
        notifyPropertyChanged(BR.saturdaySelected);
    }

    @Bindable
    public boolean isSundaySelected() {
        return _sundaySelected;
    }

    private void setSundaySelected(boolean sundaySelected) {
        _sundaySelected = sundaySelected;
        notifyPropertyChanged(BR.sundaySelected);
    }

    public void toggleMonday() {
        setMondaySelected(!isMondaySelected());
    }

    public void toggleTuesday() {
        setTuesdaySelected(!isTuesdaySelected());
    }

    public void toggleWednesday() {
        setWednesdaySelected(!isWednesdaySelected());
    }

    public void toggleThursday() {
        setThursdaySelected(!isThursdaySelected());
    }

    public void toggleFriday() {
        setFridaySelected(!isFridaySelected());
    }

    public void toggleSaturday() {
        setSaturdaySelected(!isSaturdaySelected());
    }

    public void toggleSunday() {
        setSundaySelected(!isSundaySelected());
    }

}
