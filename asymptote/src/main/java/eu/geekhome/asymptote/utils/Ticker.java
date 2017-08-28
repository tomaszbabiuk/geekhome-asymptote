package eu.geekhome.asymptote.utils;

import android.os.Handler;
import android.os.Looper;

public class Ticker {

    public interface Listener {
        void onTick();
        void onElapsed();
    }

    private int _interval;

    private int _maxRounds;
    private Handler _handler;
    private int _counter = 0;
    private boolean _enabled;

    private Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            tick();
        }
    };

    private Listener _listener;

    public Ticker(Listener listener, int maxRounds, int interval) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener mustn't be null");
        }

        _listener = listener;
        _interval = interval;
        _maxRounds = maxRounds;
        _handler = new Handler(Looper.getMainLooper());
        _enabled = true;
        tick();
    }

    private void tick() {
        _counter++;
        if (_enabled) {
            _listener.onTick();
            if (_maxRounds > 0 && _counter >= _maxRounds) {
                _counter = 0;
                if (_enabled) {
                    _listener.onElapsed();
                }
            } else {
                _handler.postDelayed(tickRunnable, _interval);
            }
        }
    }

    public void stop() {
        _enabled = false;
    }
}
