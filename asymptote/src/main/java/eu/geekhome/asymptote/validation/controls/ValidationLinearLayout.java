package eu.geekhome.asymptote.validation.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import eu.geekhome.asymptote.validation.ValidationContainer;
import eu.geekhome.asymptote.validation.ValidationContext;

public class ValidationLinearLayout extends LinearLayout implements ValidationContainer {

    private ValidationContext _validationContext;

    public ValidationLinearLayout(Context context) {
        super(context);
    }

    public ValidationLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ValidationLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ValidationContext getValidationContext() {
        return _validationContext;
    }

    @Override
    public void setValidationContext(ValidationContext context) {
        _validationContext = context;
    }
}
