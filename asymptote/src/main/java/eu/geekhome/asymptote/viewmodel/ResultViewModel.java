package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentResultBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;

public class ResultViewModel extends HelpViewModelBase<FragmentResultBinding> {
    private String _title;
    private String _status;
    private boolean _success;

    @Inject
    NavigationService _navigationService;

    public ResultViewModel(ActivityComponent activityComponent, String title, String status, boolean success) {
        super(activityComponent);

        setTitle(title);
        setStatus(status);
        setSuccess(success);
    }

    public void onDone(@NonNull View view) {
        _navigationService.goBackTo(MainViewModel.class);
    }

    @Override
    public FragmentResultBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentResultBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Bindable
    public String getStatus() {
        return _status;
    }

    public void setStatus(String status) {
        notifyPropertyChanged(BR.status);
        _status = status;
    }

    @Bindable
    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        notifyPropertyChanged(BR.title);
        _title = title;
    }

    @Bindable
    public boolean isSuccess() {
        return _success;
    }

    public void setSuccess(boolean success) {
        _success = success;
    }

    @Override
    protected String getNoWiFiRationale() {
        return null;
    }
}