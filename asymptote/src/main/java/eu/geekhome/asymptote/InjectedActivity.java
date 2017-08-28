package eu.geekhome.asymptote;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;

import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityModule;

abstract class InjectedActivity extends AppCompatActivity {
    private ActivityComponent _activityComponent;

    @UiThread
    protected ActivityComponent getActivityComponent() {
        if (_activityComponent == null) {
            _activityComponent = AsymptoteApp.get(this)
                    .getApplicationComponent()
                    .newActivityComponent(new ActivityModule(this));
        }

        return _activityComponent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doInject(getActivityComponent());
    }

    protected abstract void doInject(ActivityComponent activityComponent);

}
