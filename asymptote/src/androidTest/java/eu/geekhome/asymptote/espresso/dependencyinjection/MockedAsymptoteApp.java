package eu.geekhome.asymptote.espresso.dependencyinjection;

import dagger.android.AndroidInjector;
import eu.geekhome.asymptote.AsymptoteApp;

public class MockedAsymptoteApp extends AsymptoteApp {
    @Override
    protected AndroidInjector<AsymptoteApp> createComponent() {
        return DaggerMockedAsymptoteAppComponent.builder().mockedApplicationModule(new MockedApplicationModule(this)).build();
    }
}
