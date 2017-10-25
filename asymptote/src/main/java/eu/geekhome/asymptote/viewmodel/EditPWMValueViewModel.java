package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.PWMValue;

public class EditPWMValueViewModel extends EditValueViewModelBase<PWMValue> {
    private final Context _context;
    private int _firstPWMChannel;
    private ArrayList<String> _channels;
    private String _selectedChannel;
    private int _value;

    public EditPWMValueViewModel(Context context, SensorItemViewModel sensor) {
        _context = context;
        _channels = new ArrayList<>();
        _firstPWMChannel = 0;
        if (sensor.getSyncData().getRole() == BoardRole.RGB_1PWM ||
            sensor.getSyncData().getRole() == BoardRole.RGB_2PWM) {
            _firstPWMChannel = 3;
        }
        int channelsCount = sensor.getSyncData().getPwmDuties().length;
        for (int i = _firstPWMChannel; i<channelsCount; i++) {
            _channels.add(buildChannelName(i));
        }

        setSelectedChannel(buildChannelName(_firstPWMChannel));
    }

    private String buildChannelName(int channel) {
        return _context.getString(R.string.channel_no, channel);
    }

    @Bindable
    public ArrayList<String> getChannels() {
        return _channels;
    }


    @Override
    protected PWMValue buildValue() {
        int channel = getChannels().indexOf(getSelectedChannel()) + _firstPWMChannel;
        return new PWMValue(channel, getValue());
    }

    @Override
    public void applyValue(PWMValue pwmValue) {
        String channelToSelect = buildChannelName(pwmValue.getChannel());
        setSelectedChannel(channelToSelect);
        setValue(pwmValue.getDuty());
    }

    @Bindable
    public String getSelectedChannel() {
        return _selectedChannel;
    }

    public void setSelectedChannel(String selectedChannel) {
        _selectedChannel = selectedChannel;
        notifyPropertyChanged(BR.selectedChannel);
    }

    @Bindable
    public int getValue() {
        return _value;
    }

    public void setValue(int value) {
        _value = value;
        notifyPropertyChanged(BR.value);
    }
}
