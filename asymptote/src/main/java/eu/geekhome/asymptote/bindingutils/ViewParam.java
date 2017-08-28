package eu.geekhome.asymptote.bindingutils;

import android.app.Activity;
import android.view.View;

public interface ViewParam {
    void apply(Activity activity, View view) throws Exception;
}
