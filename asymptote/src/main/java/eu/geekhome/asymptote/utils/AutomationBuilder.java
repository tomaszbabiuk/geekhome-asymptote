package eu.geekhome.asymptote.utils;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.AutomationDateTimeHumidity;
import eu.geekhome.asymptote.model.AutomationDateTimeImpulse;
import eu.geekhome.asymptote.model.AutomationDateTimePWM;
import eu.geekhome.asymptote.model.AutomationDateTimeRGB;
import eu.geekhome.asymptote.model.AutomationDateTimeRelay;
import eu.geekhome.asymptote.model.AutomationDateTimeTemperature;
import eu.geekhome.asymptote.model.AutomationSchedulerHumidity;
import eu.geekhome.asymptote.model.AutomationSchedulerImpulse;
import eu.geekhome.asymptote.model.AutomationSchedulerPWM;
import eu.geekhome.asymptote.model.AutomationSchedulerRGB;
import eu.geekhome.asymptote.model.AutomationSchedulerRelay;
import eu.geekhome.asymptote.model.AutomationSchedulerTemperature;
import eu.geekhome.asymptote.model.AutomationUnit;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.PWMValue;
import eu.geekhome.asymptote.model.ParamValue;
import eu.geekhome.asymptote.model.RGBValue;
import eu.geekhome.asymptote.model.RelayValue;
import eu.geekhome.asymptote.model.SchedulerTrigger;

public class AutomationBuilder {

    @Inject
    public AutomationBuilder() {
    }

    public Automation buildDateTimeAutomationFromNumbers(int index, long time, int unit, long param, long value, boolean enabled) throws Exception {
        DateTimeTrigger trigger = new DateTimeTrigger(time);
        AutomationUnit unitParsed = AutomationUnit.fromInt(unit);
        switch (unitParsed) {
            case Relay:
                RelayValue relayValue = buildRelayValue((int) param, value);
                return new AutomationDateTimeRelay(index, trigger, relayValue, enabled);
            case Impulse:
                return new AutomationDateTimeImpulse(index, trigger, (int)param, enabled);
            case Temperature:
                ParamValue tempValue = buildParamValue((int) param, value);
                return new AutomationDateTimeTemperature(index, trigger, tempValue, enabled);
            case Humidity:
                ParamValue humValue = buildParamValue((int) param, value);
                return new AutomationDateTimeHumidity(index, trigger, humValue, enabled);
            case Pwm:
                PWMValue pwmValue = buildPwmValue((int) param, (int) value);
                return new AutomationDateTimePWM(index, trigger, pwmValue, enabled);
            case Rgb:
                RGBValue rgbValue = buildRgbValue(param, value);
                return new AutomationDateTimeRGB(index, trigger, rgbValue, enabled);
            default:
                throw new Exception("Cannot determine type of automation!");
        }
    }

    public Automation buildSchedulerAutomationFromNumbers(int index, long time, int days, int unit, long param, long value, boolean enabled) throws Exception {
        SchedulerTrigger trigger = new SchedulerTrigger(days, time);
        AutomationUnit unitParsed = AutomationUnit.fromInt(unit);
        switch (unitParsed) {
            case Relay:
                RelayValue relayValue = buildRelayValue((int) param, value);
                return new AutomationSchedulerRelay(index, trigger, relayValue, enabled);
            case Impulse:
                return new AutomationSchedulerImpulse(index, trigger, (int) param, enabled);
            case Temperature:
                ParamValue tempValue = buildParamValue((int) param, value);
                return new AutomationSchedulerTemperature(index, trigger, tempValue, enabled);
            case Humidity:
                ParamValue humValue = buildParamValue((int) param, value);
                return new AutomationSchedulerHumidity(index, trigger, humValue, enabled);
            case Pwm:
                PWMValue pwmValue = buildPwmValue((int) param, (int) value);
                return new AutomationSchedulerPWM(index, trigger, pwmValue, enabled);
            case Rgb:
                RGBValue rgbValue = buildRgbValue(param, value);
                return new AutomationSchedulerRGB(index, trigger, rgbValue, enabled);
            default:
                throw new Exception("Cannot determine type of automation!");
        }
    }

    @NonNull
    private PWMValue buildPwmValue(int param, int value) {
        return new PWMValue(param, value);
    }

    @NonNull
    private RGBValue buildRgbValue(long param, long value) {
        int dutyBlue = (int)(value % 256);
        int dutyGreen = (int)((value - dutyBlue)/256 % 256);
        int dutyRed = (int)((value - dutyBlue - dutyGreen * 256)/65536);
        int channelBlue = (int)(param % 256);
        int channelGreen = (int)((param - channelBlue)/256 % 256);
        int channelRed = (int)(param - channelBlue - channelGreen * 256)/65536;
        PWMValue valueRed = new PWMValue(channelRed, dutyRed);
        PWMValue valueGreen = new PWMValue(channelGreen, dutyGreen);
        PWMValue valueBlue = new PWMValue(channelBlue, dutyBlue);

        return new RGBValue(valueRed, valueGreen, valueBlue);
    }

    @NonNull
    private RelayValue buildRelayValue(int param, long value) {
        return new RelayValue(param, value == 1);
    }

    @NonNull
    private ParamValue buildParamValue(int param, long value) {
        return new ParamValue(param, value);
    }
}
