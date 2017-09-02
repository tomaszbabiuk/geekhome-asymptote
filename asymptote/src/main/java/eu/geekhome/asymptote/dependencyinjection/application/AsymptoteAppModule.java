package eu.geekhome.asymptote.dependencyinjection.application;

import android.app.Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;
import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.MainActivityComponent;
import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.SplashActivityComponent;

@Module
public abstract class AsymptoteAppModule {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);

    @Binds
    @IntoMap
    @ActivityKey(SplashActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindDetailActivity(SplashActivityComponent.Builder builder);

}