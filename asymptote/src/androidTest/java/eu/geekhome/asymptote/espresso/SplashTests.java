package eu.geekhome.asymptote.espresso;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
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
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.*;

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
            SplashActivity.class, false, false);

    @Before
    public void setupMocks() {
    }

    @Test
    public void shouldShowConditionsWhenPrivacyPolicyNotAccepted() {
        when(_privacyService.isAccepted()).thenReturn(false);
        Intent startSplashIntent = new Intent(getApp(), SplashActivity.class);
        startSplashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApp().startActivity(startSplashIntent);

        onView(withId(R.id.instr_button_disagree)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldShowSigningWhenPrivacyPolicyAccepted() {
        when(_privacyService.isAccepted()).thenReturn(true);
        Intent startSplashIntent = new Intent(getApp(), SplashActivity.class);
        startSplashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApp().startActivity(startSplashIntent);

        onView(withId(R.id.instr_button_sign_in)).check(matches(isDisplayed()));
    }
}
