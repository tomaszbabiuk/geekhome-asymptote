package eu.geekhome.asymptote.dependencyinjection.application;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import eu.geekhome.asymptote.AsymptoteApp;
import eu.geekhome.asymptote.services.PrivacyService;

@ApplicationScope
@Component(modules = {
        AndroidInjectionModule.class,
        AsymptoteAppModule.class,
        ApplicationModule.class})
public interface AsymptoteAppComponent extends AndroidInjector<AsymptoteApp> {
}