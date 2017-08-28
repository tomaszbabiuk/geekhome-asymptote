package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.support.annotation.StringRes;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class SignalItemViewModel extends SelectableItemViewModel implements LayoutHolder {

    private final TroubleshootingViewModel _repairModel;
    private final Type _signalType;
    private boolean _ledOn;

    public SignalItemViewModel(TroubleshootingViewModel repairModel, Type signalType) {
        _repairModel = repairModel;
        _signalType = signalType;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_signal;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    public void selectSignal() {
        _repairModel.setSelectedSignalType(getSignalType());
        setSelected(true);
    }

    @Bindable
    public Type getSignalType() {
        return _signalType;
    }

    public void blink(int blinkerPos) {
        boolean nextState = getSignalType().getPattern()[blinkerPos % 20] == 1;
        setLedOn(nextState);
    }

    @Bindable
    public boolean isLedOn() {
        return _ledOn;
    }

    public void setLedOn(boolean ledOn) {
        _ledOn = ledOn;
        notifyPropertyChanged(BR.ledOn);
    }

    public enum Type {
        None(R.string.signal_none, R.string.signal_none_desc,
                new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
        Blinking(R.string.signal_blinking, R.string.signal_blinking_desc,
                new byte[]{1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0}),
        OneBlink(R.string.signal_1blink, R.string.signal_1blink_desc,
                new byte[]{1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
        TwoBlinks(R.string.signal_2blinks, R.string.signal_2blinks_desc,
                new byte[]{1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
        ThreeBlinks(R.string.signal_3blinks, R.string.signal_3blinks_desc,
                new byte[]{1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
        FourBlinks(R.string.signal_4blinks, R.string.signal_4blinks_desc,
                new byte[]{1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}),
        TurnedOn(R.string.signal_turned_on, R.string.signal_turned_on_desc,
                new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});

        private final int _descriptionResId;
        private final int _nameResId;
        private final byte[] _pattern;

        Type(@StringRes int nameResId, @StringRes int descriptionResId, byte[] pattern) {
            _nameResId = nameResId;
            _descriptionResId = descriptionResId;
            _pattern = pattern;
        }

        public int getDescriptionResId() {
            return _descriptionResId;
        }

        public byte[] getPattern() {
            return _pattern;
        }

        public int getNameResId() {
            return _nameResId;
        }
    }
}
