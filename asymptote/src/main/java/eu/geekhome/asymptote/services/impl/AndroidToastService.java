package eu.geekhome.asymptote.services.impl;

import android.app.Activity;
import android.widget.Toast;

import eu.geekhome.asymptote.services.ToastService;

public class AndroidToastService implements ToastService {
    private final Activity _activity;

    public AndroidToastService(Activity activity) {
        _activity = activity;
    }

    @Override
    public void makeToast(final String toast, final boolean showLongTime) {
        _activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_activity, toast, showLongTime ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
            }
        });
    }
}
