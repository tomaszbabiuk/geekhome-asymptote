package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlTempAndHumItemViewModel extends ControlItemViewModelBase implements LayoutHolder {

    ControlTempAndHumItemViewModel(SensorItemViewModel sensor) {
        super(sensor);
    }

    @Override
    public void sync(DeviceSyncData data) {
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_temp_and_hum;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }
}
