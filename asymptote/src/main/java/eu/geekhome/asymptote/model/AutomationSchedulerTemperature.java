package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerTemperature extends Automation<SchedulerTrigger, ParamValue> {

    private final int _offset;

    public AutomationSchedulerTemperature(int index, SchedulerTrigger trigger, ParamValue value, boolean enabled) {
        super(index, trigger, value, enabled, AutomationUnit.Temperature);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

    }

    @Override
    public String composeMessage(Context context) {
        return context.getString(R.string.change_temperature_by_schedule,
                ValueConverter.daysToNames(context, getTrigger().getDays()),
                ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset),
                ValueConverter.intToCelsius((int)getValue().getValue()));
    }

    @Override
    public boolean supportsRole(BoardRole role) {
        return role == BoardRole.COOLING_THERMOSTAT || role == BoardRole.HEATING_THERMOSTAT;
    }
}
