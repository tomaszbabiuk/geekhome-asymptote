package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.Automation;
import eu.geekhome.asymptote.model.DateTimeTrigger;
import eu.geekhome.asymptote.model.RelayValue;

public class AutomationDateTimeRelayItemViewModel extends BaseObservable implements LayoutHolder {

    private final Automation<DateTimeTrigger, RelayValue> _automation;

    AutomationDateTimeRelayItemViewModel(Automation<DateTimeTrigger, RelayValue> automation) {
        _automation = automation;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_automation;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public Automation<DateTimeTrigger, RelayValue> getAutomation() {
        return _automation;
    }
}
