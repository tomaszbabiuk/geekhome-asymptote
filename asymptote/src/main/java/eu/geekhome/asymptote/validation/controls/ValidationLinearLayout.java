package eu.geekhome.asymptote.validation.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import eu.geekhome.asymptote.validation.ValidationContainer;
import eu.geekhome.asymptote.validation.ValidationContext;
import eu.geekhome.asymptote.validation.Validator;

public class ValidationLinearLayout extends LinearLayout implements ValidationContainer {

    private ValidationContext _validationContext = null;
    private ValidationContext _idleValidationContext = new ValidationContext();

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
        if (_validationContext == null) {
            return _idleValidationContext;
        }

        return _validationContext;
    }

    @Override
    public void setValidationContext(ValidationContext context) {
        _validationContext = context;
        if (_idleValidationContext.getValidators() != null && _idleValidationContext.getValidators().size() > 0) {
            for (Validator validator : _idleValidationContext.getValidators()) {
                _validationContext.addValidator(validator);
            }
        }
    }
}
