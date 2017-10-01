package eu.geekhome.asymptote.model;

import android.content.Context;

public class AutomationSchedulerRelay extends Automation<SchedulerTrigger, RelayValue> {
    private final Context _context;

    public AutomationSchedulerRelay(Context context, int index, SchedulerTrigger trigger, RelayValue value) {
        super(index, trigger, value);
        _context = context;
    }

    @Override
    public String composeMessage() {
        return "dupa";
//        String state = _context.getString(getValue().getState() ? R.string.on : R.string.off);
//        return _context.getString(R.string.change_relay_at, getValue().getChannel(), state,
//                ValueConverter.daysToText(getTrigger().getDateMark()),
//                ValueConverter.secondsToText(getTrigger().getTimeMark()));
    }

}
