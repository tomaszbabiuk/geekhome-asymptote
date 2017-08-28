package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class FavoriteColorItemViewModel extends SelectableItemViewModel implements LayoutHolder {

    private ControlRGBItemViewModel _rgbModel;
    private int _color;

    FavoriteColorItemViewModel(ControlRGBItemViewModel rgbModel, int color) {
        _rgbModel = rgbModel;
        _color = color;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_favorite_color;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

    }

    @Bindable
    public int getColor() {
        return _color;
    }

    public void setColor(int color) {
        _color = color;
        notifyPropertyChanged(BR.color);
    }

    public void picked() {
        _rgbModel.colorPicked(getColor());
    }
}
