package eu.geekhome.asymptote.validation;

import android.view.View;
import android.widget.EditText;

public class CorrectPasswordRequired extends TextValidatorBase {
    private int _minLength;

    public CorrectPasswordRequired(View view, MinLengthParams minLengthParams) {
        super(view, minLengthParams.getValidationMessage());
        _minLength = minLengthParams.getMinLength();
    }

    public String extractText() {
        if (getView() instanceof EditText) {
            EditText editText = (EditText)getView();
            if (editText != null) {
                return editText.getText().toString();
            }
        }
        return null;
    }

    @Override
    protected boolean isValid(EditText editText) {
        return (editText.getText() != null && editText.getText().toString().length() >= _minLength);
    }
}
