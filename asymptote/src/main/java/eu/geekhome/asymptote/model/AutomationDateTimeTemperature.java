package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationDateTimeTemperature extends Automation<DateTimeTrigger, ParamValue> {
    public AutomationDateTimeTemperature(int index, DateTimeTrigger trigger, ParamValue value, boolean enabled) {
        super(index, trigger, value, enabled);
    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000);

        return context.getString(R.string.change_temperature_setting_to,
                ValueConverter.intToCelsius((int)getValue().getValue()), dateFormat.format(date), timeFormat.format(date));
    }

}
