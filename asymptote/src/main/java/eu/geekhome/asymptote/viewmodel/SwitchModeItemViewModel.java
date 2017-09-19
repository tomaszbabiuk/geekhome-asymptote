package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class SwitchModeItemViewModel extends BaseObservable implements LayoutHolder {
    private int _channel;
    private long _mode;

    public SwitchModeItemViewModel(int channel, long mode) {
        _channel = channel;
        _mode = mode;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_light_switch_mode;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public int getChannel() {
        return _channel;
    }

    @Bindable
    public long getMode() {
        return _mode;
    }

    public void setMode(long lockGroup) {
        _mode = lockGroup;
        notifyPropertyChanged(BR.mode);
    }
}
