package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentResultBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class ResultViewModel extends HelpViewModelBase<FragmentResultBinding> {
    private String _title;
    private String _status;
    private boolean _success;
    private final NavigationService _navigationService;

    public ResultViewModel(MainViewModelsFactory factory, WiFiHelper wifiHelper, NavigationService navigationService,
                           String title, String status, boolean success) {
        super(factory, wifiHelper, navigationService);
        _navigationService = navigationService;
        setTitle(title);
        setStatus(status);
        setSuccess(success);
    }

    public void onDone() {
        _navigationService.goBackTo(MainViewModel.class);
    }

    @Override
    public FragmentResultBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentResultBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result, container, false);
        binding.setVm(this);
        return binding;
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