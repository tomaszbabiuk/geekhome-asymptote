package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;

public class AutomationDateTimeRelay extends Automation<DateTimeTrigger, RelayValue> {
    private final Context _context;
    private final DateFormat _dateFormat;
    private final DateFormat _timeFormat;
    private final int _offset;

    public AutomationDateTimeRelay(Context context, int index, DateTimeTrigger trigger, RelayValue value) {
        super(index, trigger, value);
        _context = context;
        _dateFormat = android.text.format.DateFormat.getDateFormat(_context);
        _timeFormat = android.text.format.DateFormat.getTimeFormat(_context);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis());

    }

    @Override
    public String composeMessage() {
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000 + _offset);
        return _context.getString(getValue().getState() ? R.string.change_relay_at_on : R.string.change_relay_at_off,
                getValue().getChannel(),_dateFormat.format(date), _timeFormat.format(date));
    }

}
