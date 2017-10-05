package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.ControlEditSchedulerBinding;
import eu.geekhome.asymptote.model.SchedulerTrigger;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.utils.ByteUtils;

public class EditSchedulerViewModel extends ViewModel<ControlEditSchedulerBinding> {
    private final GeneralDialogService _generalDialogService;
    private final SensorItemViewModel _sensor;
    private final int _offset;
    private long _time;
    private long _localTime;
    private int _days;
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

        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

        setTime(time - _offset);
        setDays(1);
    }

    @Override
    public ControlEditSchedulerBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        ControlEditSchedulerBinding binding = DataBindingUtil.inflate(inflater, R.layout.control_edit_scheduler, container, false);
        binding.setVm(this);
        return binding;
    }

    public void onPickTime() {
        _generalDialogService.pickTime((int)getTime() + _offset, new GeneralDialogService.TimePickerListener() {
            @Override
            public void onTimePicked(int hourOfDay, int minute, int second) {
                int time = hourOfDay * 3600 + minute * 60 + second;
                setTime(time - _offset);
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

        setLocalTime(_time + _offset);
    }


    SchedulerTrigger buildSchedulerTrigger() {
        int days = 0;
        if (isMondaySelected()) {
            days |= 1;
        }
        if (isTuesdaySelected()) {
            days |= 1 << 1;
        }
        if (isWednesdaySelected()) {
            days |= 1 << 2;
        }
        if (isThursdaySelected()) {
            days |= 1 << 3;
        }
        if (isFridaySelected()) {
            days |= 1 << 4;
        }
        if (isSaturdaySelected()) {
            days |= 1 << 5;
        }
        if (isSundaySelected()) {
            days |= 1 << 6;
        }
        return new SchedulerTrigger(days, getTime());
    }

    void applySchedulerTrigger(SchedulerTrigger trigger) {
        setDays(trigger.getDays());
        setTime(trigger.getTimeMark());
    }


    private void setDays(int days) {
        _days = days == 0 ? 1 : days;
        setMondaySelected(ByteUtils.getBit(days, 0));
        setTuesdaySelected(ByteUtils.getBit(days, 1));
        setWednesdaySelected(ByteUtils.getBit(days, 2));
        setThursdaySelected(ByteUtils.getBit(days, 3));
        setFridaySelected(ByteUtils.getBit(days, 4));
        setSaturdaySelected(ByteUtils.getBit(days, 5));
        setSundaySelected(ByteUtils.getBit(days, 6));
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
        if (_days > 0) {
            setMondaySelected(!isMondaySelected());
        }
    }

    public void toggleTuesday() {
        if (_days > 0) {
            setTuesdaySelected(!isTuesdaySelected());
        }
    }

    public void toggleWednesday() {
        if (_days > 0) {
            setWednesdaySelected(!isWednesdaySelected());
        }
    }
    public void toggleThursday() {
        if (_days > 0) {
            setThursdaySelected(!isThursdaySelected());
        }
    }

    public void toggleFriday() {
        if (_days > 0) {
            setFridaySelected(!isFridaySelected());
        }
    }

    public void toggleSaturday() {
        if (_days > 0) {
            setSaturdaySelected(!isSaturdaySelected());
        }
    }

    public void toggleSunday() {
        if (_days > 0) {
            setSundaySelected(!isSundaySelected());
        }
    }

    @Bindable
    public long getLocalTime() {
        return _localTime;
    }

    public void setLocalTime(long localTime) {
        _localTime = localTime;
        notifyPropertyChanged(BR.localTime);
    }
}
