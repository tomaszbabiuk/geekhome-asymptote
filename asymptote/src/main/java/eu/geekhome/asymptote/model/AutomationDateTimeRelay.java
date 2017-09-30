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
        String state = _context.getString(getValue().getState() ? R.string.on : R.string.off);
        return _context.getString(R.string.change_relay_at, getValue().getChannel(), state,
                ValueConverter.daysToText(getTrigger().getDateMark()),
                ValueConverter.secondsToText(getTrigger().getTimeMark()));
    }

}
