package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerRelay extends Automation<SchedulerTrigger, RelayValue> {

    private final int _offset;

    public AutomationSchedulerRelay(int index, SchedulerTrigger trigger, RelayValue value) {
        super(index, trigger, value);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

    }

    @Override
    public String composeMessage(Context context) {
        return context.getString(getValue().getState() ? R.string.change_relay_by_schedule_on : R.string.change_relay_by_schedule_off,
                ValueConverter.daysToNames(context, getTrigger().getDays()),
                ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset),
                getValue().getChannel());

    }
}
