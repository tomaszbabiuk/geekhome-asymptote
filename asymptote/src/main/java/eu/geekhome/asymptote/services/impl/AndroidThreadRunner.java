package eu.geekhome.asymptote.services.impl;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import eu.geekhome.asymptote.services.ThreadRunner;

public class AndroidThreadRunner implements ThreadRunner {
    private final Activity _activity;
    private final Handler _handler;

    public AndroidThreadRunner(Activity activity) {
        _activity = activity;
        _handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        _activity.runOnUiThread(runnable);
    }

    @Override
    public void runInBackground(final Runnable runnable) {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                runnable.run();
                return null;
            }
        };
        task.execute();
    }

    @Override
    public void postDelayed(Runnable runnable, long delayMillis) {
        _handler.postDelayed(runnable, delayMillis);
    }

    @Override
    public void removeCallbacks(Runnable runnable) {
        _handler.removeCallbacks(runnable);
    }
}
