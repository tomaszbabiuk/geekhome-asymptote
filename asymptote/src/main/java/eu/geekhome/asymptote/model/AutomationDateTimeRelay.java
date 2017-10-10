package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

import eu.geekhome.asymptote.R;

public class AutomationDateTimeRelay extends Automation<DateTimeTrigger, RelayValue> {
    public AutomationDateTimeRelay(int index, DateTimeTrigger trigger, RelayValue value, boolean enabled) {
        super(index, trigger, value, enabled);
    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000);
        return context.getString(getValue().getState() ? R.string.change_relay_at_on : R.string.change_relay_at_off,
                getValue().getChannel(),dateFormat.format(date), timeFormat.format(date));
    }

}
