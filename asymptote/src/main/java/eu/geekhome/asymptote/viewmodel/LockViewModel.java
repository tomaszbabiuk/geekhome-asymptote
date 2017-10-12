package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentLockBinding;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.validation.ValidationContext;

public class LockViewModel extends ViewModel<FragmentLockBinding> {
    private final SensorItemViewModel _sensor;
    private final ValidationContext _validation = new ValidationContext();
    private String _password;
    private String _confirmPassword;
    private boolean _rememberPassword;
    private boolean _blocked;

    private final NavigationService _navigationService;
    private final SyncManager _syncManager;
    private final ToastService _toastService;
    private final CloudUserService _cloudUserService;
    private final ThreadRunner _threadRunner;
    private final EmergencyManager _emergencyManager;
    private HelpActionBarViewModel _actionBarModel;


    public LockViewModel(MainViewModelsFactory factory, NavigationService navigationService, SyncManager syncManager, ToastService toastService,
                         CloudUserService cloudUserService, ThreadRunner threadRunner,
                         EmergencyManager emergencyManager, SensorItemViewModel sensor) {
        _sensor = sensor;
        _navigationService = navigationService;
        _syncManager = syncManager;
        _toastService = toastService;
        _cloudUserService = cloudUserService;
        _threadRunner = threadRunner;
        _emergencyManager = emergencyManager;
        _actionBarModel = factory.createHelpActionBarModel();
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
    }

    @Override
    public FragmentLockBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentLockBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lock, container, false);
        binding.setVm(this);
        return binding;
    }


    public void lock() {
        if (_validation.validate()) {
            setBlocked(true);
            _syncManager.lock(_sensor.getSyncData().getSystemInfo().getVariant(), _sensor.getAddress(), new SyncManager.SyncCallback() {
                @Override
                public void success() {
                    _threadRunner.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            _emergencyManager.setPassword(getPassword());
                            _sensor.getSyncData().setLocked(true);
                            _sensor.requestSyncDelayed();
                        }
                    });

                    if (isRememberPassword()) {
                        _cloudUserService.rememberEmergencyPassword(_sensor.getUserId(), getPassword(), new CloudActionCallback<Void>() {
                            @Override
                            public void success(Void data) {
                                _navigationService.goBackTo(MainViewModel.class);
                                setBlocked(false);
                            }

                            @Override
                            public void failure(CloudException exception) {
                                setBlocked(false);
                                _toastService.makeToast(exception.getLocalizedMessage(), true);
                            }
                        });
                    } else {
                        setBlocked(false);
                    }
                }

                @Override
                public void failure(Exception exception) {
                    setBlocked(false);
                    _toastService.makeToast(exception.getLocalizedMessage(), false);
                }
            }, getPassword());

            if (!isRememberPassword()) {
                _navigationService.goBackTo(MainViewModel.class);
            }
        } else {
            setBlocked(false);
        }
    }

    @Bindable
    public String getConfirmPassword() {
        return _confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        _confirmPassword = confirmPassword;
        notifyPropertyChanged(BR.confirmPassword);
    }

    @Bindable
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public boolean isEmergency() {
        return _emergencyManager.isEmergency();
    }

    @Bindable
    public boolean isRememberPassword() {
        return _rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword) {
        _rememberPassword = rememberPassword;
        notifyPropertyChanged(BR.rememberPassword);
    }

    @Bindable
    public boolean isBlocked() {
        return _blocked;
    }

    public void setBlocked(boolean blocked) {
        _blocked = blocked;
        notifyPropertyChanged(BR.blocked);
    }
}
