package eu.geekhome.asymptote.viewmodel;

import android.content.Context;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.DeviceSyncData;

public class ControlHygrostatItemViewModel extends ControlThermostatItemViewModel {
    public ControlHygrostatItemViewModel(SensorItemViewModel sensor, Context context,
                                         int channel, DeviceSyncData syncData, String activeMessage, String inactiveMessage) {
        super(sensor, context, channel, syncData, activeMessage, inactiveMessage);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_hygrostat;
    }
}
