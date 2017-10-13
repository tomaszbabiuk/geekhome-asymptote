package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;

public class EditImpulseValueViewModel extends BaseObservable {
    private final Context _context;
    private ArrayList<String> _channels;
    private String _selectedChannel;

    public EditImpulseValueViewModel(Context context, SensorItemViewModel sensor) {
        _context = context;
        _channels = new ArrayList<>();
        int channelsCount = sensor.getSyncData().getRelayStates().length;
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

    int buildImpulseValue() {
        return getChannels().indexOf(getSelectedChannel());
    }

    public void applyImpulseValue(int channel) {
        String channelToSelect = buildChannelName(channel);
        setSelectedChannel(channelToSelect);
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
