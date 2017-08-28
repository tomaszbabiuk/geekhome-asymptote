package eu.geekhome.asymptote.bindingutils.controls;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

public class BindableSwitch extends SwitchCompat{

    public BindableSwitch(Context context) {
        super(context);
    }

    public BindableSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BindableSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
    }
}
