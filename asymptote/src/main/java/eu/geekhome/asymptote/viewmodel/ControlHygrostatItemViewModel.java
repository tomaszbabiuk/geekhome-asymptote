package eu.geekhome.asymptote.viewmodel;

import android.content.Context;

import eu.geekhome.asymptote.R;

public class ControlHygrostatItemViewModel extends ControlThermostatItemViewModel {
    public ControlHygrostatItemViewModel(SensorItemViewModel sensor, Context context,
                                         int channel, long[] initialParams, String activeMessage, String inactiveMessage) {
        super(sensor, context, channel, initialParams, activeMessage, inactiveMessage);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_hygrostat;
    }
}
