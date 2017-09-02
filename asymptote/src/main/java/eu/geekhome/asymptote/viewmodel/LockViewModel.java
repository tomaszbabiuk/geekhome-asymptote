package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.DialogLockBinding;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.validation.ValidationContext;

public class LockViewModel extends ViewModel<DialogLockBinding> {
    private final MainViewModel _mainViewModel;
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
    private final Context _context;
    private final ThreadRunner _threadRunner;
    private final EmergencyManager _emergencyManager;


    public LockViewModel(NavigationService navigationService, SyncManager syncManager, ToastService toastService,
                         CloudUserService cloudUserService, Context context, ThreadRunner threadRunner,
                         EmergencyManager emergencyManager, MainViewModel mainViewModel, SensorItemViewModel sensor) {
        _mainViewModel = mainViewModel;
        _sensor = sensor;
        _navigationService = navigationService;
        _syncManager = syncManager;
        _toastService = toastService;
        _cloudUserService = cloudUserService;
        _context = context;
        _threadRunner = threadRunner;
        _emergencyManager = emergencyManager;
    }

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
    }

    @Override
    public DialogLockBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        DialogLockBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_lock, container, false);
        binding.setVm(this);
        Animation rotateAnimation = AnimationUtils.loadAnimation(_context, R.anim.rotate_around_center_point_linear);
        binding.gearImage.startAnimation(rotateAnimation);
        return binding;
    }

    public void close() {
        _navigationService.goBack();
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
                            _mainViewModel.rediscover();
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
