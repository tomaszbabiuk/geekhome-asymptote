package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.RGBValue;

public class EditRGBValueViewModel extends EditValueViewModelBase<RGBValue>  {
    private final Context _context;
    private ArrayList<String> _channels;
    private String _selectedChannel;
    private int _value;

    public EditRGBValueViewModel(Context context, SensorItemViewModel sensor) {
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


    @Override
    protected RGBValue buildValue() {
//        int channel = getChannels().indexOf(getSelectedChannel());
//        return new PWMValue(channel, getValue());
        return null;
    }

    @Override
    public void applyValue(RGBValue value) {
//        String channelToSelect = buildChannelName(pwmValue.getChannel());
//        setSelectedChannel(channelToSelect);
//        setValue(pwmValue.getDuty());
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
