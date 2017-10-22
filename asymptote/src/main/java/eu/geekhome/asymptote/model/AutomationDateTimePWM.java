package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

import eu.geekhome.asymptote.R;

public class AutomationDateTimePWM extends Automation<DateTimeTrigger, PWMValue> {
    public AutomationDateTimePWM(int index, DateTimeTrigger trigger, PWMValue value, boolean enabled) {
        super(index, trigger, value, enabled, AutomationUnit.Pwm);
    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000);
        String intensity =  Math.round((float)getValue().getDuty()/255 * 100) + "%";

        return context.getString(R.string.change_pwm_to,
                getValue().getChannel(), intensity,
                dateFormat.format(date), timeFormat.format(date));
    }

    @Override
    public boolean supportsRole(BoardRole role) {
        return role == BoardRole.MULTI_PWM || role == BoardRole.RGB_1PWM || role == BoardRole.RGB_2PWM;
    }
}
