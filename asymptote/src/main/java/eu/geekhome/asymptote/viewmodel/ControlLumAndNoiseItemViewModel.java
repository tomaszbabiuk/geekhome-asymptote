package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlLumAndNoiseItemViewModel extends ControlItemViewModelBase implements LayoutHolder {

    public ControlLumAndNoiseItemViewModel(SensorItemViewModel sensor) {
        super(sensor);
    }

    @Override
    public void sync(DeviceSyncData data) {

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_lum_and_noise;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }
}
