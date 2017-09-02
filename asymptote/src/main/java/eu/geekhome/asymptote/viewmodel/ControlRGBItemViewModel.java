package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Color;

import java.util.Collection;
import java.util.Locale;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.ColorPickedListener;
import eu.geekhome.asymptote.services.FavoriteColorsService;
import eu.geekhome.asymptote.services.impl.RankedColor;

public class ControlRGBItemViewModel extends ControlItemViewModelBase implements LayoutHolder {

    private final ColorDialogService _colorDialogService;
    private final FavoriteColorsService _favoriteColorsService;

    private final ObservableArrayList<LayoutHolder> _favoriteColors;
    private int _channelRed;
    private int _channelGreen;
    private int _channelBlue;
    private int _channelWhite;
    private int _valueRed;
    private int _valueGreen;
    private int _valueBlue;

    public ControlRGBItemViewModel(SensorItemViewModel sensor, ColorDialogService colorDialogService,
                                   FavoriteColorsService favoriteColorsService,
                                   int channelRed, int channelGreen, int channelBlue, int channelWhite,
                                   int valueRed, int valueGreen, int valueBlue) {
        super(sensor);
        _favoriteColors = new ObservableArrayList<>();
        _favoriteColorsService = favoriteColorsService;
        _colorDialogService = colorDialogService;
        _channelRed = channelRed;
        _channelGreen = channelGreen;
        _channelBlue = channelBlue;
        _channelWhite = channelWhite;
        setValueRed(valueRed);
        setValueGreen(valueGreen);
        setValueBlue(valueBlue);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_rgb;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    public void addColor() {
        int color = Color.rgb(getSensor().getSyncData().getPwmDuties()[_channelRed],
                getSensor().getSyncData().getPwmDuties()[_channelGreen],
                getSensor().getSyncData().getPwmDuties()[_channelBlue]);
        _colorDialogService.pick(color == Color.BLACK ? Color.WHITE : color, new ColorPickedListener() {
            @Override
            public void colorPicked(int color) {
                ControlRGBItemViewModel.this.colorPicked(color);
            }
        });
    }

    public void colorPicked(final int color) {
        String setId = composeSetId(getSensor().getSyncData().getDeviceKey().getDeviceId());

        if (color != Color.BLACK) {
            _favoriteColorsService.colorPicked(setId, color);
        }

        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        setValueRed(red);
        setValueGreen(green);
        setValueBlue(blue);

        pwmChanged(_channelRed, red);
        pwmChanged(_channelGreen, green);
        pwmChanged(_channelBlue, blue);

        if (_channelWhite != -1) {
            //handle RGBW
            int white = (int) Math.round(0.2126 * red + 0.7152 * green + 0.0722 * blue);
            pwmChanged(_channelWhite, white);
            getSensor().onRequestFullSync();
        } else {
            //handle RGB
            getSensor().onRequestFullSync();
        }

        Collection<RankedColor> rankedColors = _favoriteColorsService.getFavoriteColors(setId);
        mergeFavoriteColors(rankedColors, color);
    }

    private String composeSetId(String deviceId) {
        return String.format(Locale.ENGLISH, "%s.%d.%d.%d.%d",
                deviceId,
                _channelRed, _channelGreen, _channelBlue, _channelWhite);
    }

    private void mergeFavoriteColors(Collection<RankedColor> rankedColors, int selectedColor) {
        if (rankedColors != null) {
            int i = 0;
            for (RankedColor rankedColor : rankedColors) {
                boolean selected = rankedColor.getColor() == selectedColor;
                if (_favoriteColors.size() < i + 1) {
                    FavoriteColorItemViewModel model = new FavoriteColorItemViewModel(this, rankedColor.getColor());
                    _favoriteColors.add(model);
                    model.setSelected(selected);
                } else {
                    FavoriteColorItemViewModel favoriteColorViewModel = (FavoriteColorItemViewModel) _favoriteColors.get(i);
                    favoriteColorViewModel.setColor(rankedColor.getColor());
                    favoriteColorViewModel.setSelected(selected);
                }

                i++;
            }
        }
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getFavoriteColors() {
        return _favoriteColors;
    }


    @Bindable
    public int getValueRed() {
        return _valueRed;
    }

    public void setValueRed(int valueRed) {
        _valueRed = valueRed;
        notifyPropertyChanged(BR.valueRed);
    }

    @Bindable
    public int getValueGreen() {
        return _valueGreen;
    }

    public void setValueGreen(int valueGreen) {
        _valueGreen = valueGreen;
        notifyPropertyChanged(BR.valueGreen);
    }

    @Bindable
    public int getValueBlue() {
        return _valueBlue;
    }

    public void setValueBlue(int valueBlue) {
        _valueBlue = valueBlue;
        notifyPropertyChanged(BR.valueBlue);
    }

    @Override
    public void sync(DeviceSyncData data) {
        String setId = composeSetId(data.getDeviceKey().getDeviceId());
        Collection<RankedColor> rankedColors = _favoriteColorsService.getFavoriteColors(setId);
        int selectedColor = Color.rgb(data.getPwmDuties()[0], data.getPwmDuties()[1], data.getPwmDuties()[2]);
        boolean selectedColorExists = false;
        for (RankedColor rankedColor : rankedColors) {
            if (rankedColor.getColor() == selectedColor) {
                selectedColorExists = true;
                break;
            }
        }
        if (!selectedColorExists && selectedColor != Color.BLACK) {
            rankedColors.add(new RankedColor(selectedColor));
        }
        mergeFavoriteColors(rankedColors, selectedColor);
    }
}
