package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.PWMValue;

public class EditPWMValueViewModel extends BaseObservable {
    private final Context _context;
    private ArrayList<String> _channels;
    private String _selectedChannel;
    private int _value;

    public EditPWMValueViewModel(Context context, SensorItemViewModel sensor) {
        _context = context;
        _channels = new ArrayList<>();
        int channelsCount = sensor.getSyncData().getPwmDuties().length;
        for (int i=0; i<channelsCount; i++) {
            _channels.add(buildChannelName(i));
        }

        setSelectedChannel(buildChannelName(0));
    }

    private String buildChannelName(int channel) {
        return _context.getString(R.string.channel_no, channel);
    }

    @Bindable
    public ArrayList<String> getChannels() {
        return _channels;
    }


    PWMValue buildPWMValue() {
        int channel = getChannels().indexOf(getSelectedChannel());
        return new PWMValue(channel, getValue());
    }

    public void applyPWMValue(PWMValue pwmValue) {
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
