package eu.geekhome.asymptote.services;

public interface ThreadRunner {
    void runOnUiThread(Runnable runnable);
    void runInBackground(Runnable runnable);
    void postDelayed(Runnable runnable, long delayMillis);

    void removeCallbacks(Runnable runnable);
}
