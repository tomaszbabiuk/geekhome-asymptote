package eu.geekhome.asymptote.validation;

import android.view.View;
import android.widget.EditText;

public class FieldRequired extends TextValidatorBase {
    public FieldRequired(View view, String validationMessage) {
        super(view, validationMessage);
    }

    @Override
    protected boolean isValid(EditText editText) {
        return (editText.getText() != null && !editText.getText().toString().isEmpty());
    }
}
