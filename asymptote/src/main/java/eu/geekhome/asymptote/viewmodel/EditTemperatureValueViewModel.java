package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.utils.ValueConverter;

public class EditTemperatureValueViewModel extends EditValueViewModelBase<ParamValue> {
    private ArrayList<String> _values;
    private String _selectedValue;

    public EditTemperatureValueViewModel(SensorItemViewModel sensor) {
        _values = new ArrayList<>();

        long minValue = sensor.getSyncData().getParams()[2];
        long maxValue = sensor.getSyncData().getParams()[3];
        for (long i=minValue; i <= maxValue; i+= 50) {
            _values.add(ValueConverter.intToCelsius((int)i));
        }

        setSelectedValue(ValueConverter.intToCelsius((int)minValue));
    }

    @Bindable
    public ArrayList<String> getValues() {
        return _values;
    }

    @Bindable
    public String getSelectedValue() {
        return _selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        _selectedValue = selectedValue;
        notifyPropertyChanged(BR.selectedValue);
    }

    @Override
    protected ParamValue buildValue() {
        int value = ValueConverter.celsiusToInt(getSelectedValue());
        return new ParamValue(0, value);
    }

    @Override
    public void applyValue(ParamValue value) {
        String valueName = ValueConverter.intToCelsius((int)value.getValue());
        setSelectedValue(valueName);
    }
}
