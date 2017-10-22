package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerPWM extends Automation<SchedulerTrigger, PWMValue> {

    private final int _offset;

    public AutomationSchedulerPWM(int index, SchedulerTrigger trigger, PWMValue value, boolean enabled) {
        super(index, trigger, value, enabled, AutomationUnit.Pwm);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;
    }

    @Override
    public String composeMessage(Context context) {
        String intensity =  Math.round(getValue().getDuty()/255 * 100) + "%";

        return context.getString(R.string.change_pwm_setting_by_schedule,
                ValueConverter.daysToNames(context, getTrigger().getDays()),
                ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset),
                getValue().getChannel(),
                intensity);
    }

    @Override
    public boolean supportsRole(BoardRole role) {
        return role == BoardRole.MULTI_PWM || role == BoardRole.RGB_1PWM || role == BoardRole.RGB_2PWM;
    }
}
