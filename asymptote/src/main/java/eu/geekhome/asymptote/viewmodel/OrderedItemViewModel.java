package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class OrderedItemViewModel extends BaseObservable implements LayoutHolder {

    private final int _number;
    private final String _content;

    public OrderedItemViewModel(int number, String content) {
        _number = number;
        _content = content;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_ordered;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

    }

    @Bindable
    public int getNumber() {
        return _number;
    }

    @Bindable
    public String getContent() {
        return _content;
    }
}
