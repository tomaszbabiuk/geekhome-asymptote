package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerHumidity extends Automation<SchedulerTrigger, ParamValue> {

    private final int _offset;

    public AutomationSchedulerHumidity(int index, SchedulerTrigger trigger, ParamValue value, boolean enabled) {
        super(index, trigger, value, enabled);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

    }

    @Override
    public String composeMessage(Context context) {
        return context.getString(R.string.change_humidity_by_schedule,
                ValueConverter.daysToNames(context, getTrigger().getDays()),
                ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset),
                ValueConverter.intToHumidity((int)getValue().getValue()));
    }

    @Override
    public boolean supportsRole(BoardRole role) {
        return role == BoardRole.DRYING_HYGROSTAT || role == BoardRole.HUMIDIFICATION_HYGROSTAT;
    }
}
