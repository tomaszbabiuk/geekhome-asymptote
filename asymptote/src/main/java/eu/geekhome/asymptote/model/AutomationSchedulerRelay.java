package eu.geekhome.asymptote.model;

import android.content.Context;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ByteUtils;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerRelay extends Automation<SchedulerTrigger, RelayValue> {
    private final Context _context;

    public AutomationSchedulerRelay(Context context, int index, SchedulerTrigger trigger, RelayValue value) {
        super(index, trigger, value);
        _context = context;
    }

    @Override
    public String composeMessage() {
        StringBuilder daysTextBuilder = new StringBuilder();
        if (ByteUtils.getBit(getTrigger().getDays(),0)) {
            daysTextBuilder.append(_context.getString(R.string.at_monday)).append(", ");
        }
        if (ByteUtils.getBit(getTrigger().getDays(),1)) {
            daysTextBuilder.append(_context.getString(R.string.at_tuesday)).append(", ");
        }
        if (ByteUtils.getBit(getTrigger().getDays(),2)) {
            daysTextBuilder.append(_context.getString(R.string.at_wednesday)).append(", ");
        }
        if (ByteUtils.getBit(getTrigger().getDays(),3)) {
            daysTextBuilder.append(_context.getString(R.string.at_thursday)).append(", ");
        }
        if (ByteUtils.getBit(getTrigger().getDays(),4)) {
            daysTextBuilder.append(_context.getString(R.string.at_friday)).append(", ");
        }
        if (ByteUtils.getBit(getTrigger().getDays(),5)) {
            daysTextBuilder.append(_context.getString(R.string.at_saturday)).append(", ");
        }
        if (ByteUtils.getBit(getTrigger().getDays(),6)) {
            daysTextBuilder.append(_context.getString(R.string.sunday)).append(", ");
        }

        return _context.getString(getValue().getState() ? R.string.change_relay_by_schedule_on : R.string.change_relay_by_schedule_off,
                daysTextBuilder.toString(),
                ValueConverter.secondsToText(getTrigger().getTimeMark()),
                getValue().getChannel());

    }
}
