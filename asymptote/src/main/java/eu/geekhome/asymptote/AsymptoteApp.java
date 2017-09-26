package eu.geekhome.asymptote;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import eu.geekhome.asymptote.dependencyinjection.application.ApplicationModule;
import eu.geekhome.asymptote.dependencyinjection.application.AsymptoteAppComponent;
import eu.geekhome.asymptote.dependencyinjection.application.DaggerAsymptoteAppComponent;

public class AsymptoteApp extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> _dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        setComponent(createComponent());
    }

    protected AsymptoteAppComponent createComponent() {
        return DaggerAsymptoteAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return _dispatchingAndroidInjector;
    }

    public void setComponent(AsymptoteAppComponent component) {
        component.inject(this);
    }
}