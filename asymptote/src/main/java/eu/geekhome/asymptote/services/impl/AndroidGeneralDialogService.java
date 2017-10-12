package eu.geekhome.asymptote.services.impl;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.services.GeneralDialogService;

public class AndroidGeneralDialogService implements GeneralDialogService, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {
    private Context _context;
    private FragmentManager _fragmentManager;
    private TimePickerListener _timeListener;
    private DatePickerListener _dateListener;

    public AndroidGeneralDialogService(Context context, FragmentManager fragmentManager) {
        _context = context;
        _fragmentManager = fragmentManager;
    }

    @Override
    public void pickTime(int seconds, boolean enableSeconds, TimePickerListener listener) {
        int hourOfDay = seconds / 3600;
        int minutes = seconds % 3600 / 60;
        int secs = seconds % 60;
        TimePickerDialog dpd = TimePickerDialog.newInstance(this,hourOfDay,minutes,secs, true);
        dpd.enableSeconds(enableSeconds);
        dpd.show(_fragmentManager, "TimePickerDialog");
        _timeListener = listener;
    }

    @Override
    public void pickDate(int date, DatePickerListener listener) {
        int year = date / (31*12);
        int month = date % (31*12) / 31;
        int day = date % 31;
        DatePickerDialog dpd = DatePickerDialog.newInstance(this,year,month,day);
        dpd.setMinDate(Calendar.getInstance());
        dpd.show(_fragmentManager, "DatePickerDialog");
        _dateListener = listener;
    }


    @Override
    public void showYesNoDialog(@StringRes int messageId, final Runnable yesRunnable, final Runnable noRunnable) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        if (yesRunnable != null) {
                            yesRunnable.run();
                        }
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        if (noRunnable != null) {
                            noRunnable.run();
                        }
                        break;
                }
            }
        };

        String message = _context.getString(messageId);
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(message)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener)
                .show();
    }

    @Override
    public void showOKDialog(@StringRes int messageId, final Runnable okRunnable) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEUTRAL:
                        if (okRunnable != null) {
                            okRunnable.run();
                        }
                        break;
                }
            }
        };

        String message = _context.getString(messageId);
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(message)
                .setNeutralButton(R.string.ok, dialogClickListener)
                .show();
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
            if (_timeListener != null) {
                _timeListener.onTimePicked(hourOfDay, minute, second);
            }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (_dateListener != null) {
            _dateListener.onDataPicked(year, monthOfYear, dayOfMonth);
        }
    }
}
