package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerGate extends Automation<SchedulerTrigger, String> {

    private final int _offset;

    public AutomationSchedulerGate(int index, SchedulerTrigger trigger, String value, boolean enabled) {
        super(index, trigger, value, enabled);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

    }

    @Override
    public String composeMessage(Context context) {

        if (getValue().equals("*doopen")) {
            return context.getString(R.string.change_gate_setting_to_open_by_schedule,
                    ValueConverter.daysToNames(context, getTrigger().getDays()),
                    ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset));
        }
        if (getValue().equals("*doclose")) {
            return context.getString(R.string.change_gate_setting_to_closed_by_schedule,
                    ValueConverter.daysToNames(context, getTrigger().getDays()),
                    ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset));
        }
        if (getValue().equals("*dorelease")) {
            return context.getString(R.string.change_gate_setting_to_released_by_schedule,
                    ValueConverter.daysToNames(context, getTrigger().getDays()),
                    ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset));
        }
        if (getValue().equals("*dostop")) {
            return context.getString(R.string.change_gate_setting_to_blocked_by_schedule,
                    ValueConverter.daysToNames(context, getTrigger().getDays()),
                    ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset));
        }

        return context.getString(R.string.error_invalid_gate_automation_parameters);
    }
}
