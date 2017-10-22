package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.Color;

import java.util.ArrayList;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.RGBValue;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.ColorPickedListener;

public class EditRGBValueViewModel extends EditValueViewModelBase<RGBValue> {
    private final Context _context;
    private final ColorDialogService _colorDialogService;
    private ArrayList<String> _channels;
    private String _selectedChannel;
    private int _selectedRed;
    private int _selectedGreen;
    private int _selectedBlue;

    public EditRGBValueViewModel(Context context, SensorItemViewModel sensor, ColorDialogService colorDialogService) {
        _context = context;
        _colorDialogService = colorDialogService;
        _channels = new ArrayList<>();
        int channelsCount = sensor.getSyncData().getPwmDuties().length;
        for (int i = 0; i < channelsCount; i++) {
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

    public void pickColor() {
        int color = Color.rgb(getSelectedRed(), getSelectedGreen(), getSelectedBlue());
        _colorDialogService.pick(color == Color.BLACK ? Color.WHITE : color, new ColorPickedListener() {
            @Override
            public void colorPicked(int color) {
                EditRGBValueViewModel.this.colorPicked(color);
            }
        });
    }

    private void colorPicked(int color) {
        setSelectedRed(Color.red(color));
        setSelectedGreen(Color.green(color));
        setSelectedBlue(Color.blue(color));
    }

    @Bindable
    public int getSelectedRed() {
        return _selectedRed;
    }

    public void setSelectedRed(int selectedRed) {
        _selectedRed = selectedRed;
        notifyPropertyChanged(BR.selectedRed);
    }

    @Bindable
    public int getSelectedGreen() {
        return _selectedGreen;
    }

    public void setSelectedGreen(int selectedGreen) {
        _selectedGreen = selectedGreen;
        notifyPropertyChanged(BR.selectedGreen);
    }

    @Bindable
    public int getSelectedBlue() {
        return _selectedBlue;
    }

    public void setSelectedBlue(int selectedBlue) {
        _selectedBlue = selectedBlue;
        notifyPropertyChanged(BR.selectedBlue);
    }
}