package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DateTimeTriggerValue;
import eu.geekhome.asymptote.model.RelayValue;

public class TriggerDateTimeRelayItemViewModel extends BaseObservable implements LayoutHolder {

    private final DateTimeTriggerValue<RelayValue> _value;

    TriggerDateTimeRelayItemViewModel(DateTimeTriggerValue<RelayValue> value) {
        _value = value;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_trigger_datetime_relay;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public DateTimeTriggerValue<RelayValue> getValue() {
        return _value;
    }
}
