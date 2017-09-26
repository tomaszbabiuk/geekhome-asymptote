package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.BoardRole;

public class RoleItemViewModel extends SelectableItemViewModel implements LayoutHolder {
    private EditSensorViewModel _editModel;
    private BoardRole _role;
    private String _name;
    private String _description;

    RoleItemViewModel(EditSensorViewModel editModel, BoardRole role, String name, String description) {
        _editModel = editModel;
        _role = role;
        _name = name;
        _description = description;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_role;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {

    }

    @Bindable
    public BoardRole getRole() {
        return _role;
    }

    @Bindable
    public String getName() {
        return _name;
    }

    @Bindable
    public String getDescription() {
        return _description;
    }

    public void selectRole() {
        _editModel.setNewRole(getRole());
        setSelected(true);
    }
}
