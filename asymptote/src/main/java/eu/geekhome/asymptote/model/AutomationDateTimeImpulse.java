package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

import eu.geekhome.asymptote.R;

public class AutomationDateTimeImpulse extends Automation<DateTimeTrigger, Integer> {
    public AutomationDateTimeImpulse(int index, DateTimeTrigger trigger, Integer value, boolean enabled) {
        super(index, trigger, value, enabled);
    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000);

        return context.getString(R.string.change_impulse_setting_to,
                getValue(),
                dateFormat.format(date), timeFormat.format(date));
    }

}
