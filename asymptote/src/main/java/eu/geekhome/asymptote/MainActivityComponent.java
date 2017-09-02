package eu.geekhome.asymptote;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityScope;
import eu.geekhome.asymptote.dependencyinjection.activity.MainActivityModule;

@ActivityScope
@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent extends AndroidInjector<MainActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}
}