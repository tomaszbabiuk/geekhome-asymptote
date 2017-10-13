package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerImpulse extends Automation<SchedulerTrigger, Integer> {

    private final int _offset;

    public AutomationSchedulerImpulse(int index, SchedulerTrigger trigger, Integer value, boolean enabled) {
        super(index, trigger, value, enabled);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

    }

    @Override
    public String composeMessage(Context context) {

        
        return context.getString(R.string.change_impulse_setting_by_schedule,
                ValueConverter.daysToNames(context, getTrigger().getDays()),
                ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset),
                getValue());
    }

    @Override
    public boolean supportsRole(BoardRole role) {
        return role == BoardRole.MAINS1_ADV || role == BoardRole.MAINS2_ADV || role == BoardRole.MAINS4_ADV;
    }
}
