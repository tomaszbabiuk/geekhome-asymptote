package eu.geekhome.asymptote.services.impl;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.services.GeneralDialogService;

public class AndroidGeneralDialogService implements GeneralDialogService, TimePickerDialog.OnTimeSetListener {
    private Context _context;
    private FragmentManager _fragmentManager;
    private TimePickerListener _listener;

    public AndroidGeneralDialogService(Context context, FragmentManager fragmentManager) {
        _context = context;
        _fragmentManager = fragmentManager;
    }

    @Override
    public void pickTime(int seconds, TimePickerListener listener) {
        int hourOfDay = seconds / 3600;
        int minutes = seconds % 3600 / 60;
        int secs = seconds % 60;
        TimePickerDialog dpd = TimePickerDialog.newInstance(this,hourOfDay,minutes,secs, true);
        dpd.enableSeconds(true);
        dpd.show(_fragmentManager, "Datepickerdialog");
        _listener = listener;
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
            if (_listener != null) {
                _listener.onTimePicked(hourOfDay, minute, second);
            }
    }
}
