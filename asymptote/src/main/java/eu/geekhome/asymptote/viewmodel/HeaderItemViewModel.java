package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class HeaderItemViewModel extends BaseObservable implements LayoutHolder {

    private final Style _style;
    private final String _content;

    public HeaderItemViewModel(Style style, String content) {
        _style = style;
        _content = content;
    }

    @Override
    public int getItemLayoutId() {
        return  _style == Style.Sub ? R.layout.listitem_header_sub : R.layout.listitem_header_main;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

    }

    @Bindable
    public String getContent() {
        return _content;
    }

    public enum Style {
        Main,
        Sub
    }
}
