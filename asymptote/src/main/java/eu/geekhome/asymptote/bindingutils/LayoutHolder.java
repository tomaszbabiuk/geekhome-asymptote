package eu.geekhome.asymptote.bindingutils;

import android.databinding.ViewDataBinding;

public interface LayoutHolder {
    int getItemLayoutId();
    void onBinding(ViewDataBinding binding);
}
