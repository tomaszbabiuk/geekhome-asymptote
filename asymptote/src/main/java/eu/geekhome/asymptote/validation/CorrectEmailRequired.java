package eu.geekhome.asymptote.validation;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class CorrectEmailRequired extends TextValidatorBase {
    public CorrectEmailRequired(View view, String validationMessage) {
        super(view, validationMessage);
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected boolean isValid(EditText editText) {
        return isValidEmail(editText.getText());
    }
}
