package eu.geekhome.asymptote.model;

import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.utils.ByteUtils;
import eu.geekhome.asymptote.utils.ValueConverter;

public class AutomationSchedulerRGB extends Automation<SchedulerTrigger, RGBValue> {

    private final int _offset;

    public AutomationSchedulerRGB(int index, SchedulerTrigger trigger, RGBValue value, boolean enabled) {
        super(index, trigger, value, enabled);
        _offset = TimeZone.getDefault().getOffset(Calendar.getInstance().getTimeInMillis())/1000;

    }

    @Override
    public String composeMessage(Context context) {
        String colorHex = "#" + ByteUtils.bytesToHex(new byte[]{
                (byte) getValue().getRed().getDuty(),
                (byte) getValue().getGreen().getDuty(),
                (byte) getValue().getBlue().getDuty()
        });

        return context.getString(R.string.change_rgb_setting_by_schedule,
                ValueConverter.daysToNames(context, getTrigger().getDays()),
                ValueConverter.secondsToText(getTrigger().getTimeMark() + _offset),
                colorHex);
    }

    @Override
    public boolean supportsRole(BoardRole role) {
        return role == BoardRole.RGBW || role == BoardRole.RGB_1PWM || role == BoardRole.RGB_2PWM;
    }
}
