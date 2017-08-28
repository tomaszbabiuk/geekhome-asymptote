package eu.geekhome.asymptote.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import eu.geekhome.asymptote.BR;

public class CloudUser extends BaseObservable {
    private String _email;
    private String _id;
    private boolean _isEmailVerified;

    public CloudUser(String id, String email, boolean isEmailVerified) {
        setId(id);
        setEmail(email);
        setEmailVerified(isEmailVerified);
    }

    @Bindable
    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public boolean isEmailVerified() {
        return _isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        _isEmailVerified = emailVerified;
        notifyPropertyChanged(BR.emailVerified);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CloudUser)) {
            return false;
        }

        boolean areEmailsEqual = false;
        if (getEmail() != null && ((CloudUser) obj).getEmail() != null) {
            areEmailsEqual = getEmail().equals(((CloudUser) obj).getEmail());
        }

        boolean areIdsEqual = false;
        if (getId() != null && (((CloudUser) obj).getId() != null)) {
            areIdsEqual = getId().equals(((CloudUser) obj).getId());
        }

        return areEmailsEqual && areIdsEqual;
    }
}