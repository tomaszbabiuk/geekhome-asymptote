package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Calendar;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.ControlEditDatetimeBinding;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.services.GeneralDialogService;

public class EditDateTimeViewModel extends ViewModel<ControlEditDatetimeBinding> {
    private final GeneralDialogService _generalDialogService;
    private final SensorItemViewModel _sensor;
    private long _time;
    private long _date;

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public EditDateTimeViewModel(GeneralDialogService generalDialogService,
                                 SensorItemViewModel sensor) {
        _generalDialogService = generalDialogService;
        _sensor = sensor;
        Calendar now = Calendar.getInstance();
        int hours = now.get(Calendar.HOUR_OF_DAY);
        int minutes = now.get(Calendar.MINUTE);
        int seconds = now.get(Calendar.SECOND);
        int time = seconds + minutes * 60 + hours * 3600;
        setTime(time);

        int years = now.get(Calendar.YEAR);
        int months = now.get(Calendar.MONTH);
        int days = now.get(Calendar.DAY_OF_MONTH);
        int date = days + months * 31 + years * 12 * 31;
        setDate(date);

    }

    @Override
    public ControlEditDatetimeBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        ControlEditDatetimeBinding binding = DataBindingUtil.inflate(inflater, R.layout.control_edit_datetime, container, false);
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

    DateTimeTrigger buildDataTimeTrigger() {
        return new DateTimeTrigger(getDate(), getTime());
    }

    public void applyDateTime(DateTimeTrigger trigger) {
        setDate(trigger.getDateMark());
        setTime(trigger.getTimeMark());
    }
}
