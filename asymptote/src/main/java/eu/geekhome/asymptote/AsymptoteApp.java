package eu.geekhome.asymptote;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import eu.geekhome.asymptote.dependencyinjection.application.ApplicationModule;
import eu.geekhome.asymptote.dependencyinjection.application.DaggerAsymptoteAppComponent;

public class AsymptoteApp extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAsymptoteAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build().
                inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}