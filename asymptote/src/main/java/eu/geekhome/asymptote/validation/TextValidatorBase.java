package eu.geekhome.asymptote.validation;

import android.view.View;
import android.widget.EditText;

import java.io.InvalidClassException;

abstract class TextValidatorBase implements Validator {
    private View _view;
    private String _validationMessage;

    public TextValidatorBase(View view, String validationMessage) {
        if (view == null) {
            throw new IllegalArgumentException("View must not be null!");
        }

        _view = view;
        _validationMessage = validationMessage;
    }

    @Override
    public boolean validate() throws InvalidClassException {

        if (_view instanceof EditText) {
            EditText editText = (EditText) _view;
            editText.setError(null);

            boolean isValid = isValid(editText);
            if (!isValid) {
                editText.setError(_validationMessage);
            }

            return isValid;
        }

        String exceptionMessage = String.format("This class of %s cannot be validated by %s validator!",
                _view.getClass().getSimpleName(), getClass().getSimpleName());
        throw new InvalidClassException(exceptionMessage);
    }

    protected abstract boolean isValid(EditText editText);

    protected View getView() {
        return _view;
    }
}
