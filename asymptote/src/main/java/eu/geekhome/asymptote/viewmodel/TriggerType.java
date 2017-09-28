package eu.geekhome.asymptote.viewmodel;

import android.support.annotation.RawRes;
import android.support.annotation.StringRes;

import eu.geekhome.asymptote.R;

public enum TriggerType {
    DateTimeOfRelay(R.raw.calendar, R.string.datetime_trigger_name, R.string.datetime_trigger_desc),
    Scheduler(R.raw.calendar2, R.string.scheduled_trigger_name, R.string.scheduled_trigger_desc);

    private final int _iconId;
    private final int _nameResId;
    private final int _descriptionResId;

    TriggerType(@RawRes int iconId, @StringRes int nameResId,  @StringRes int descriptionResId) {
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
