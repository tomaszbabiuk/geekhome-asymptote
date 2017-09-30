package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.RelayValue;

public class EditRelayValueViewModel  extends BaseObservable {
    private final Context _context;
    private ArrayList<String> _channels;
    private ArrayList<String> _values;
    private String _selectedValue;
    private String _selectedChannel;

    public EditRelayValueViewModel(Context context, SensorItemViewModel sensor) {
        _context = context;
        _channels = new ArrayList<>();
        int channelsCount = sensor.getSyncData().getRelayStates().length;
        for (int i=0; i<channelsCount; i++) {
            _channels.add(buildChannelName(i));
        }
        _values = new ArrayList<>();
        _values.add(context.getString(R.string.on));
        _values.add(context.getString(R.string.off));
    }

    private String buildChannelName(int channel) {
        return _context.getString(R.string.channel_no, channel);
    }

    @Bindable
    public ArrayList<String> getChannels() {
        return _channels;
    }

    @Bindable
    public ArrayList<String> getValues() {
        return _values;
    }

    RelayValue buildRelayValue() {
        int channel = getChannels().indexOf(getSelectedChannel());
        boolean state = getValues().indexOf(getSelectedValue()) == 0;
        return new RelayValue(channel, state);
    }

    public void applyRelayValue(RelayValue relayValue) {
        String channelToSelect = buildChannelName(relayValue.getChannel());
        setSelectedChannel(channelToSelect);
        String valueName = _context.getString(relayValue.getState() ? R.string.on : R.string.off);
        setSelectedValue(valueName);
    }

    @Bindable
    public String getSelectedValue() {
        return _selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        _selectedValue = selectedValue;
        notifyPropertyChanged(BR.selectedValue);
    }

    @Bindable
    public String getSelectedChannel() {
        return _selectedChannel;
    }

    public void setSelectedChannel(String selectedChannel) {
        _selectedChannel = selectedChannel;
        notifyPropertyChanged(BR.selectedChannel);
    }
}
