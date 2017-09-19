package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class RelayInterlockItemViewModel extends BaseObservable implements LayoutHolder {
    private int _channel;
    private long _lockGroup;
    private int _totalChannels;

    RelayInterlockItemViewModel(int channel, long lockGroup, int totalChannels) {
        _channel = channel;
        _lockGroup = lockGroup;
        _totalChannels = totalChannels;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_interlock;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public int getChannel() {
        return _channel;
    }

    @Bindable
    public long getLockGroup() {
        return _lockGroup;
    }

    public void setLockGroup(long lockGroup) {
        _lockGroup = lockGroup;
        notifyPropertyChanged(BR.lockGroup);
    }

    @Bindable
    public int getTotalChannels() {
        return _totalChannels;
    }
}
