package eu.geekhome.asymptote.espresso;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import eu.geekhome.asymptote.AsymptoteApp;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.dependencyinjection.application.ApplicationModule;
import eu.geekhome.asymptote.dependencyinjection.application.AsymptoteAppComponent;
import eu.geekhome.asymptote.services.PrivacyService;
import it.cosenonjaviste.daggermock.DaggerMockRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SplashTests {

    private static AsymptoteApp getApp() {
        return (AsymptoteApp) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    @Mock
    PrivacyService _privacyService;

    @Rule
    public DaggerMockRule<AsymptoteAppComponent> mockitoRule =
        new DaggerMockRule<>(AsymptoteAppComponent.class, new ApplicationModule(getApp()))
                .set(new DaggerMockRule.ComponentSetter<AsymptoteAppComponent>() {
                    @Override
                    public void setComponent(AsymptoteAppComponent component) {
                        getApp().setComponent(component);
                    }
                });


    @Rule
    public ActivityTestRule<SplashActivity> mActivityRule = new ActivityTestRule<>(
            SplashActivity.class, true, false);

    private void startSplashActivity() {
        Intent startSplashIntent = new Intent(getApp(), SplashActivity.class);
        startSplashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApp().startActivity(startSplashIntent);
    }

    @Test
    public void shouldShowConditionsWhenPrivacyPolicyNotAccepted() {
        when(_privacyService.isAccepted()).thenReturn(false);
        startSplashActivity();

        onView(withId(R.id.instr_fragment_privacy)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowSigningWhenPrivacyPolicyAccepted() {
        when(_privacyService.isAccepted()).thenReturn(true);
        startSplashActivity();

        onView(withId(R.id.instr_fragment_sign_in)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowResetPassword() {
        when(_privacyService.isAccepted()).thenReturn(true);
        startSplashActivity();

        onView(withId(R.id.instr_button_goto_reset_password)).perform(click());
        onView(withId(R.id.instr_fragment_reset_password_dark)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldStartEmergencyMode() {
        when(_privacyService.isAccepted()).thenReturn(true);
        startSplashActivity();

        onView(withId(R.id.instr_button_emergency_mode)).perform(click());
        onView(withId(R.id.instr_fragment_main)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldStartSignIn() {
        when(_privacyService.isAccepted()).thenReturn(true);
        startSplashActivity();

        onView(withId(R.id.instr_button_sign_in)).perform(click());
        onView(withId(R.id.instr_fragment_login_with_email)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldSignInWithEmail() {
        when(_privacyService.isAccepted()).thenReturn(true);
        startSplashActivity();

        onView(withId(R.id.instr_button_sign_in)).perform(click());
        onView(withId(R.id.instr_text_login_email)).perform(typeText("testdude@test.test"));
        onView(withId(R.id.instr_text_login_password)).perform(scrollTo(), typeText("yolo!"));
        onView(withId(R.id.instr_fragment_login_with_email)).perform(scrollTo(), click());


        onView(withId(R.id.instr_text_login_sign_in)).perform(click());
    }
}
