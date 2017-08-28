package eu.geekhome.asymptote.bindingutils.viewparams;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewParam;

public class ShowBackButtonInToolbarViewParam implements ViewParam {
    @Override
    public void apply(Activity activity, View view) throws Exception {
        if (activity instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            if (toolbar != null) {
                appCompatActivity.setSupportActionBar(toolbar);
                ActionBar supportBar = appCompatActivity.getSupportActionBar();
                if (supportBar != null) {
                    supportBar.setDisplayHomeAsUpEnabled(true);
                    supportBar.setDisplayShowHomeEnabled(true);
                }
            }
        } else {
            throw new Exception(getClass().getSimpleName() + " can be only applied to " + AppCompatActivity.class.getSimpleName());
        }
    }
}
