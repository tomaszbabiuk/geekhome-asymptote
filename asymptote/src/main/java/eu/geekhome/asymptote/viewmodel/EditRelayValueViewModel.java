package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.ControlEditRelayValueBinding;

public class EditRelayValueViewModel extends ViewModel<ControlEditRelayValueBinding> {
    private ArrayList<String> _channels;
    private ArrayList<String> _states;

    public EditRelayValueViewModel(Context context, SensorItemViewModel sensor) {
        _channels = new ArrayList<>();
        int channelsCount = sensor.getSyncData().getRelayStates().length;
        for (int i=0; i<channelsCount; i++) {
            _channels.add(context.getString(R.string.channel_no, i));
        }
        _states = new ArrayList<>();
        _states.add(context.getString(R.string.on));
        _states.add(context.getString(R.string.off));
    }

    @Override
    public ControlEditRelayValueBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        ControlEditRelayValueBinding binding = DataBindingUtil.inflate(inflater, R.layout.control_edit_relay_value, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ArrayList<String> getChannels() {
        return _channels;
    }

    @Bindable
    public ArrayList<String> getStates() {
        return _states;
    }
}
