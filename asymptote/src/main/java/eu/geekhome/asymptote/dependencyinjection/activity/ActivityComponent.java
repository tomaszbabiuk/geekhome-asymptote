package eu.geekhome.asymptote.dependencyinjection.activity;


import dagger.Subcomponent;
import eu.geekhome.asymptote.MainActivity;
import eu.geekhome.asymptote.SplashActivity;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {
    void inject(SplashActivity splashActivity);
    void inject(MainActivity mainActivity);
}
