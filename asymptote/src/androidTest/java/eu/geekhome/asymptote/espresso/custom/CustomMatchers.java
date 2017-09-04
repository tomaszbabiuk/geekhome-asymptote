package eu.geekhome.asymptote.espresso.custom;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.test.InstrumentationRegistry;
import android.view.View;
import android.widget.EditText;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import eu.geekhome.asymptote.AsymptoteApp;

public class CustomMatchers {
    public static Matcher<View> hasTextInputLayoutErrorText(@StringRes final int expectedTextResourceId) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                CharSequence error = ((EditText) view).getError();

                if (error == null) {
                    return false;
                }

                String hint = error.toString();
                String expectedErrorText = getApp().getString(expectedTextResourceId);

                return expectedErrorText.equals(hint);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }

    private static AsymptoteApp getApp() {
        return (AsymptoteApp) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }
}
