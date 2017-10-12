package eu.geekhome.asymptote.services;

import android.support.annotation.StringRes;

public interface GeneralDialogService {
    void pickTime(int seconds, boolean enableSeconds, TimePickerListener listener);
    void pickDate(int date, DatePickerListener listener);
    void showYesNoDialog(@StringRes int messageId, Runnable yesRunnable, Runnable noRunnable);
    void showOKDialog(@StringRes int messageId, Runnable okRunnable);

    interface TimePickerListener {
        void onTimePicked(int hourOfDay, int minute, int second);
    }

    interface DatePickerListener {
        void onDataPicked(int year, int month, int day);
    }
}
