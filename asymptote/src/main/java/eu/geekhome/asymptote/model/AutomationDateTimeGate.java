package eu.geekhome.asymptote.model;

import android.content.Context;

import java.text.DateFormat;
import java.util.Date;

import eu.geekhome.asymptote.R;

public class AutomationDateTimeGate extends Automation<DateTimeTrigger, String> {
    public AutomationDateTimeGate(int index, DateTimeTrigger trigger, String value, boolean enabled) {
        super(index, trigger, value, enabled);
    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000);

        if (getValue().equals("*doopen")) {
            return context.getString(R.string.change_gate_to_open,
                    dateFormat.format(date), timeFormat.format(date));
        }
        if (getValue().equals("*doclose")) {
            return context.getString(R.string.change_gate_setting_to_closed,
                    dateFormat.format(date), timeFormat.format(date));
        }
        if (getValue().equals("*dorelease")) {
            return context.getString(R.string.change_gate_setting_to_released,
                    dateFormat.format(date), timeFormat.format(date));
        }
        if (getValue().equals("*dostop")) {
            return context.getString(R.string.change_gate_setting_to_blocked,
                    dateFormat.format(date), timeFormat.format(date));
        }

        return context.getString(R.string.error_invalid_gate_automation_parameters);
    }

}
