package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import com.android.databinding.library.baseAdapters.BR;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.GeneralDialogService;

public class RelayWorkingModeItemViewModel extends BaseObservable implements LayoutHolder {
    @Inject GeneralDialogService _generalDialogService;
    private long _impulse;
    private int _channel;

    public RelayWorkingModeItemViewModel(ActivityComponent activityComponent, long impulse, int channel) {
        activityComponent.inject(this);
        _impulse = impulse;
        _channel = channel;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_relay_working_mode;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

    }

    public void setImpulse(long impulse) {
        _impulse = impulse;
        notifyPropertyChanged(BR.impulse);
    }

    @Bindable
    public long getImpulse() {
        return _impulse;
    }

    public void onPickTime() {
        final int initialImpulse = getImpulse() < 0 ? 0 : (int)getImpulse();
        _generalDialogService.pickTime(initialImpulse, new GeneralDialogService.TimePickerListener() {
            @Override
            public void onTimePicked(int hourOfDay, int minute, int second) {
                long impulse = hourOfDay * 3600 + minute * 60 + second;
                if (initialImpulse < 0) {
                    impulse = impulse * -1;
                }

                setImpulse(impulse);
            }
        });
    }

    public void toggle() {
        if (getImpulse() == 0 || getImpulse() == 1) {
            setImpulse(30);
        } else {
            setImpulse(getImpulse() * -1);
        }
    }

    @Bindable
    public int getChannel() {
        return _channel;
    }
}
