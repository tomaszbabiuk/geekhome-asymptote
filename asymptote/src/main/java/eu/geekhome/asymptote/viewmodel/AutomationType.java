package eu.geekhome.asymptote.viewmodel;

import android.support.annotation.RawRes;
import android.support.annotation.StringRes;

import eu.geekhome.asymptote.R;

public enum AutomationType {
    None(-1, -1, -1),
    DateTimeOfRelay(R.raw.calendar, R.string.automation_datetime_name, R.string.automation_datetime_relay_desc),
    DateTimeOfTemperature(R.raw.calendar, R.string.automation_datetime_name, R.string.automation_datetime_temperature_desc),
    DateTimeOfHumidity(R.raw.calendar, R.string.automation_datetime_name, R.string.automation_datetime_humidity_desc),
    SchedulerOfRelay(R.raw.calendar2, R.string.automation_scheduler_name, R.string.automation_scheduler_relay_desc),
    SchedulerOfTemperature(R.raw.calendar2, R.string.automation_scheduler_name, R.string.automation_scheduler_temperature_desc),
    SchedulerOfHumidity(R.raw.calendar2, R.string.automation_scheduler_name, R.string.automation_scheduler_humidity_desc);

    private final int _iconId;
    private final int _nameResId;
    private final int _descriptionResId;

    AutomationType(@RawRes int iconId, @StringRes int nameResId,  @StringRes int descriptionResId) {
        _iconId = iconId;
        _nameResId = nameResId;
        _descriptionResId = descriptionResId;
    }

    public int getIconId() {
        return _iconId;
    }

    public int getNameResId() {
        return _nameResId;
    }

    public int getDescriptionResId() {
        return _descriptionResId;
    }
}
