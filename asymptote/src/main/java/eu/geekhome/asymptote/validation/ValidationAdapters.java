package eu.geekhome.asymptote.validation;

import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewParent;

@SuppressWarnings("unused")
public class ValidationAdapters {
    @BindingAdapter({"validateRequiredField"})
    public static void setValidateRequiredField(View view, String validationMessage) {
        ValidationContainer validationContainer = findValidatedContainerUp(view);
        if (validationContainer != null) {
            validationContainer
                    .getValidationContext()
                    .addValidator(new FieldRequired(view, validationMessage));
        }
    }

    @BindingAdapter({"validateEmail"})
    public static void setValidateEmail(View view, String validationMessage) {
        ValidationContainer validationContainer = findValidatedContainerUp(view);
        if (validationContainer != null) {
            validationContainer
                    .getValidationContext()
                    .addValidator(new CorrectEmailRequired(view, validationMessage));
        }
    }

    @BindingAdapter({"validatePassword"})
    public static void setValidatePassword(View view, MinLengthParams minLength) {
        ValidationContainer validationContainer = findValidatedContainerUp(view);
        if (validationContainer != null) {
            validationContainer
                    .getValidationContext()
                    .addValidator(new CorrectPasswordRequired(view, minLength));
        }
    }

    @BindingAdapter({"validatePasswordConfirmed"})
    public static void setValidateRepeatPassword(View view, String validationMessage) {
        ValidationContainer validationContainer = findValidatedContainerUp(view);
        if (validationContainer != null) {
            ValidationContext context = validationContainer.getValidationContext();
            validationContainer
                    .getValidationContext()
                    .addValidator(new ConfirmedPasswordRepeated(view, validationMessage, context));
        }
    }

    private static ValidationContainer findValidatedContainerUp(View view) {
        ViewParent iteratedView = view.getParent();
        while (iteratedView != null) {
            if (iteratedView instanceof ValidationContainer) {
                return (ValidationContainer)iteratedView;
            }

            iteratedView = iteratedView.getParent();
        }

        return null;
    }

    @BindingAdapter({"validationContext"})
    public static void setValidationContext(View view, ValidationContext validationContext) {
        if (view instanceof ValidationContainer) {
            ValidationContainer validationContainer = (ValidationContainer)view;
            validationContainer.setValidationContext(validationContext);
        }
    }
}
