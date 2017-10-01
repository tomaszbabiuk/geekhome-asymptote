package eu.geekhome.asymptote.model;

import android.content.Context;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationDateTimeRelay extends Automation<DateTimeTrigger, RelayValue> {
    private final Context _context;

    public AutomationDateTimeRelay(Context context, int index, DateTimeTrigger trigger, RelayValue value) {
        super(index, trigger, value);
        _context = context;
    }

    @Override
    public String composeMessage() {
        return _context.getString(getValue().getState() ? R.string.change_relay_at_on : R.string.change_relay_at_off,
                ValueConverter.daysToText(getTrigger().getDateMark()),
                ValueConverter.secondsToText(getTrigger().getTimeMark()),
                getValue().getChannel());
    }

}
