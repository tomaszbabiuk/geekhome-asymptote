package eu.geekhome.asymptote.espresso.dependencyinjection;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import eu.geekhome.asymptote.AsymptoteApp;
import eu.geekhome.asymptote.dependencyinjection.application.ApplicationScope;
import eu.geekhome.asymptote.dependencyinjection.application.AsymptoteAppModule;

@ApplicationScope
@Component(modules = {
        AndroidInjectionModule.class,
        AsymptoteAppModule.class,
        MockedApplicationModule.class})
public interface MockedAsymptoteAppComponent extends AndroidInjector<AsymptoteApp> {
}