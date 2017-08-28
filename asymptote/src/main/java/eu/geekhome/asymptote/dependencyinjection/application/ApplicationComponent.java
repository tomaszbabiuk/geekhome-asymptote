package eu.geekhome.asymptote.dependencyinjection.application;

import android.app.Application;

import dagger.Component;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityModule;

@ApplicationScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(Application asymptoteApp);

    ActivityComponent newActivityComponent(ActivityModule activityModule);
}
