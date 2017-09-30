package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.Automation;

public class AutomationItemViewModel extends BaseObservable implements LayoutHolder {

    private Automation _automation;
    private final EditAutomationViewModel _parent;
    private String _message;

    AutomationItemViewModel(Automation automation, EditAutomationViewModel parent) {
        _automation = automation;
        _parent = parent;
        updateMessage();
    }

    private void updateMessage() {
        setMessage(_automation.composeMessage());
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_automation;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

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
        _parent.deleteAutomation(this);
    }

    public void edit() {
        _parent.editAutomation(this);
    }
}
