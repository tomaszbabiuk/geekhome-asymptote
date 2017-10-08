package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.utils.ValueConverter;

public class EditHumidityValueViewModel extends BaseObservable {
    private ArrayList<String> _values;
    private String _selectedValue;

    public EditHumidityValueViewModel(SensorItemViewModel sensor) {
        _values = new ArrayList<>();

        long minValue = sensor.getSyncData().getParams()[2];
        long maxValue = sensor.getSyncData().getParams()[3];
        for (long i=minValue; i <= maxValue; i+= 50) {
            _values.add(ValueConverter.intToHumidity((int)i));
        }

        setSelectedValue(ValueConverter.intToHumidity((int)minValue));
    }

    @Bindable
    public ArrayList<String> getValues() {
        return _values;
    }

    ParamValue buildHumidityValue() {
        int value = ValueConverter.humidityToInt(getSelectedValue());
        return new ParamValue(0, value);
    }

    void applyHumidityValue(ParamValue paramValue) {
        String valueName = ValueConverter.intToHumidity((int)paramValue.getValue());
        setSelectedValue(valueName);
    }

    @Bindable
    public String getSelectedValue() {
        return _selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        _selectedValue = selectedValue;
        notifyPropertyChanged(BR.selectedValue);
    }
}
