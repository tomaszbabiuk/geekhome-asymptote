package eu.geekhome.asymptote.viewmodel;

import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class ControlSeparatorViewModel implements LayoutHolder {

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_control_separator;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }
}
