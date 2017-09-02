package eu.geekhome.asymptote.espresso;

import android.app.Application;
import android.content.Context;
import android.support.test.runner.AndroidJUnitRunner;

import eu.geekhome.asymptote.espresso.dependencyinjection.MockedAsymptoteApp;

public class MockTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, MockedAsymptoteApp.class.getName(), context);
    }
}