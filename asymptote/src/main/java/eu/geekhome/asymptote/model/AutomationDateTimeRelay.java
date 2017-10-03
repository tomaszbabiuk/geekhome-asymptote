package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;

public class AutomationDateTimeRelay extends Automation<DateTimeTrigger, RelayValue> {
    private DateFormat _dateFormat;
    private DateFormat _timeFormat;
    private final int _offset;

    public AutomationDateTimeRelay(int index, DateTimeTrigger trigger, RelayValue value) {
        super(index, trigger, value);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());

    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000 + _offset);
        return context.getString(getValue().getState() ? R.string.change_relay_at_on : R.string.change_relay_at_off,
                getValue().getChannel(),dateFormat.format(date), timeFormat.format(date));
    }

}
