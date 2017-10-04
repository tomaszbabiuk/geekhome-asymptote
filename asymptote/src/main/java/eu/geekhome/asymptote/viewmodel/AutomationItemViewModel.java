package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.graphics.Paint;
import android.widget.TextView;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.databinding.ListitemAutomationBinding;
import eu.geekhome.asymptote.model.Automation;

public class AutomationItemViewModel extends BaseObservable implements LayoutHolder {

    private boolean _loading;
    private Automation _automation;
    private final EditAutomationViewModel _parent;
    private String _message;
    private TextView _textView;
    private boolean _remove;

    AutomationItemViewModel(Context context, Automation automation, EditAutomationViewModel parent) {
        _automation = automation;
        _parent = parent;
        _loading = false;
        updateMessage(context);
    }

    private void updateMessage(Context context) {
        setMessage(_automation.composeMessage(context));
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_automation;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        _textView = ((ListitemAutomationBinding)binding).textview;
    }

    @Bindable
    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
        notifyPropertyChanged(BR.message);
    }

    @Bindable
    public Automation getAutomation() {
        return _automation;
    }

    public void delete() {
        //_parent.deleteAutomation(this);
        if ((_textView.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0) {
            setRemove(false);
            _textView.setPaintFlags(_textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        } else {
            setRemove(true);
            _textView.setPaintFlags(_textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public void edit() {
        _parent.editAutomation(this);
    }

    @Bindable
    public boolean isRemove() {
        return _remove;
    }

    public void setRemove(boolean remove) {
        _remove = remove;
        notifyPropertyChanged(BR.remove);
    }

    @Bindable
    public boolean isLoading() {
        return _loading;
    }

    public void setLoading(boolean loading) {
        _loading = loading;
        notifyPropertyChanged(BR.loading);
    }
}
