package eu.geekhome.asymptote;

import android.app.Application;
import android.content.Context;
import android.support.annotation.UiThread;

import eu.geekhome.asymptote.dependencyinjection.application.ApplicationComponent;
import eu.geekhome.asymptote.dependencyinjection.application.ApplicationModule;
import eu.geekhome.asymptote.dependencyinjection.application.DaggerApplicationComponent;

public class AsymptoteApp extends Application {
    private ApplicationComponent _applicationComponent;

    public static AsymptoteApp get(Context context) {
        return (AsymptoteApp) context.getApplicationContext();
    }

    @UiThread
    public ApplicationComponent getApplicationComponent() {
        if (_applicationComponent == null) {
            _applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }

        return _applicationComponent;
    }

    @Override
    public void onCreate() {
        getApplicationComponent().inject(this);
        super.onCreate();
    }
}
