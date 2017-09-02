package eu.geekhome.asymptote;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityScope;
import eu.geekhome.asymptote.dependencyinjection.activity.SplashActivityModule;

@ActivityScope
@Subcomponent(modules = SplashActivityModule.class)
public interface SplashActivityComponent extends AndroidInjector<SplashActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<SplashActivity>{}
}