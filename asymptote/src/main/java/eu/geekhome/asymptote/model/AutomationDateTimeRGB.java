package eu.geekhome.asymptote.model;

import android.content.Context;
import java.text.DateFormat;
import java.util.Date;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ByteUtils;

public class AutomationDateTimeRGB extends Automation<DateTimeTrigger, RGBValue> {
    public AutomationDateTimeRGB(int index, DateTimeTrigger trigger, RGBValue value, boolean enabled) {
        super(index, trigger, value, enabled);
    }

    @Override
    public String composeMessage(Context context) {
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(context);
        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(context);
        Date date = new Date(getTrigger().getUtcTimestamp() * 1000);
        String colorHex = "#" + ByteUtils.bytesToHex(new byte[]{
                (byte) getValue().getRed().getDuty(),
                (byte) getValue().getGreen().getDuty(),
                (byte) getValue().getBlue().getDuty()
        });

        return context.getString(R.string.change_rgb_to,
                colorHex,
                dateFormat.format(date), timeFormat.format(date));
    }

}
