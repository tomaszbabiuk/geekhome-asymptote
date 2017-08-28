package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class BoardUnsupportedViewModel extends BaseObservable implements LayoutHolder {
    private String _deviceId;

    public BoardUnsupportedViewModel(String deviceId) {
        _deviceId = deviceId;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_board_unsupported;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public String getDeviceId() {
        return _deviceId;
    }
}
