package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;

import eu.geekhome.asymptote.BR;

public abstract class ValueSync<T> extends ControlItemViewModelBase {
    private T _value;
    private int _channel;
    private int _waitCounter;
    private String _message;
    private Context _context;

    public ValueSync(SensorItemViewModel sensor, Context context, int channel, T initialValue) {
        super(sensor);
        _context = context;
        _channel = channel;
        syncValue(initialValue);
    }

    @Bindable
    public T getValue() {
        return _value;
    }

    public void setValue(T value) {
        if (value != null && !value.equals(_value)) {
            _value = value;
            _waitCounter = 0;
            execute(value);
            notifyPropertyChanged(BR.value);

            setMessage(composeWaitMessage(_context, value));
        }
    }

    protected abstract void execute(T value);

    public void syncValue(T value) {

        if (value != _value && getSensor().isBlocked() && _waitCounter < 3) {
            _waitCounter++;
        } else {
            _waitCounter = 0;
            if (isValueUpdateRequested()) {
                return;
            }

            _value = value;

            notifyPropertyChanged(BR.value);
            setMessage(composeValueMessage(_context, _value));
        }
    }

    protected abstract boolean isValueUpdateRequested();

    protected abstract String composeValueMessage(Context context, T value);

    protected abstract String composeWaitMessage(Context context, T value);

    @Bindable
    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
        notifyPropertyChanged(BR.message);
    }

    protected int getChannel() {
        return _channel;
    }
}

