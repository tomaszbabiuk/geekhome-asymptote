package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlTempItemViewModel extends ControlItemViewModelBase implements LayoutHolder {

    public ControlTempItemViewModel(SensorItemViewModel sensor) {
        super(sensor);
    }

    @Override
    public void sync(DeviceSyncData data) {
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_temp;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }
}
