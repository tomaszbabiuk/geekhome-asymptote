package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.view.View;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.databinding.ListitemFavoriteColorBinding;
import eu.geekhome.asymptote.services.GeneralDialogService;

public class FavoriteColorItemViewModel extends SelectableItemViewModel implements LayoutHolder {

    private final GeneralDialogService _generalDialogService;
    private ControlRGBItemViewModel _rgbModel;
    private int _color;

    public FavoriteColorItemViewModel(GeneralDialogService generalDialogService, ControlRGBItemViewModel rgbModel, int color) {
        _generalDialogService = generalDialogService;
        _rgbModel = rgbModel;
        _color = color;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_favorite_color;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        ((ListitemFavoriteColorBinding)binding).container.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (isSelected()) {
                            _generalDialogService.showOKDialog(R.string.cannot_remove_selected_color, null);
                        } else {
                            _generalDialogService.showYesNoDialog(R.string.remove_this_color_question, new Runnable() {
                                @Override
                                public void run() {
                                    _rgbModel.colorRemoved(FavoriteColorItemViewModel.this);
                                }
                            }, null);
                        }
                        return false;
                    }
                });
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
