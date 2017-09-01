package eu.geekhome.asymptote.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Html;
import android.text.Spanned;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.PrivacyService;

public class PrivacyViewModel extends BaseObservable {
    private final PrivacyService _privacyService;
    private final Activity _activity;
    private final Spanned _privacyText;
    private boolean _showAgreement;
    private boolean _bottomReached;

    public PrivacyViewModel(PrivacyService privacyService, Activity activity, boolean showAgreement) {
        _privacyService = privacyService;
        _activity = activity;
        _showAgreement = showAgreement;
        _privacyText = Html.fromHtml(_activity.getString(R.string.terms_and_privacy_html));
    }

    public void done() {
        _activity.startActivity(SplashActivity.createOpeningIntent(_activity, false));
    }
    public void agree() {
        _privacyService.setAgreed();
        done();
    }

    public void disagree() {
        _activity.finishAffinity();
    }

    @Bindable
    public Spanned getPrivacyText() {
        return _privacyText;
    }

    @Bindable
    public boolean isShowAgreement() {
        return _showAgreement;
    }

    @Bindable
    public boolean isBottomReached() {
        return _bottomReached;
    }

    public void setBottomReached(boolean bottomReached) {
        _bottomReached = bottomReached;
        notifyPropertyChanged(BR.bottomReached);
    }
}
