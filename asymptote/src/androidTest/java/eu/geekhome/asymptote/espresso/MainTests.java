package eu.geekhome.asymptote.espresso;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import eu.geekhome.asymptote.AsymptoteApp;
import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.dependencyinjection.application.ApplicationModule;
import eu.geekhome.asymptote.dependencyinjection.application.AsymptoteAppComponent;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.impl.FirebaseCloudDeviceService;
import eu.geekhome.asymptote.services.impl.FirebaseCloudUserService;
import it.cosenonjaviste.daggermock.DaggerMockRule;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainTests {

    private static AsymptoteApp getApp() {
        return (AsymptoteApp) InstrumentationRegistry.getInstrumentation()
                .getTargetContext().getApplicationContext();
    }

    @Mock
    FirebaseCloudUserService _firebaseCloudUserService;

    @Mock
    FirebaseCloudDeviceService _firebaseCloudDeviceService;

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
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class, true, false);

    private void startMainActivity() {
        Intent intent = new Intent(getApp(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApp().startActivity(intent);
    }

    @Captor
    private ArgumentCaptor<CloudActionCallback<UserSnapshot>> captor;

    @Test
    public void shouldBeAbleToSendEmail() {
//        Answer answer = new Answer<UserSnapshotCallback>() {
//            public UserSnapshotCallback answer(InvocationOnMock invocation) {
//                Object[] args = invocation.getArguments();
//                //Mock mock = invocation.getMock();
//                return null;
//            }};
//
//        doAnswer(answer).when(_firebaseCloudDeviceService).getUserSnapshot(anyString(), any(UserSnapshotCallback.class));
//        startMainActivity();
//        onView(withId(R.id.instr_button_help)).perform(click());
    }
}
