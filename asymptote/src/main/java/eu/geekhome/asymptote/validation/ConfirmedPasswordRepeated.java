package eu.geekhome.asymptote.validation;

import android.view.View;
import android.widget.EditText;

public class ConfirmedPasswordRepeated extends TextValidatorBase {
    private final ValidationContext _validationContext;

    public ConfirmedPasswordRepeated(View view, String validationMessage, ValidationContext validationContext) {
        super(view, validationMessage);
        _validationContext = validationContext;
    }

    @Override
    protected boolean isValid(EditText editText) {
        if (editText.getText() == null) {
            return false;
        }

        for (Validator validator : _validationContext.getValidators()) {
            if (validator instanceof CorrectPasswordRequired) {
                String password = ((CorrectPasswordRequired) validator).extractText();
                String confirmPassword = editText.getText().toString();
                return password != null && !password.isEmpty() && password.equals(confirmPassword);
            }
        }

        return false;
    }
}
