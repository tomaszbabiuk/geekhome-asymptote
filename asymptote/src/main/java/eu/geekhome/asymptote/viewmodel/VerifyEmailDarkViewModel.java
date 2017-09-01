package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentVerifyEmailDarkBinding;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.utils.Ticker;

public class VerifyEmailDarkViewModel extends ViewModel<FragmentVerifyEmailDarkBinding> {
    private final CloudUserService _cloudUserService;
    private final ToastService _toastService;
    private final NavigationService _navigationService;
    private final CloudDeviceService _cloudDeviceService;
    private final Context _context;
    private String _email;
    private String _password;
    private SplashViewModel _splashViewModel;
    private Ticker _verificationTicker;
    private String _instruction;
    private boolean _emailVerificationInProgress;

    @Bindable
    public String getInstruction() {
        return _instruction;
    }

    public void setInstruction(String instruction) {
        _instruction = instruction;
    }

    @Bindable
    public boolean isEmailVerificationInProgress() {
        return _emailVerificationInProgress;
    }

    private void setEmailVerificationInProgress(boolean emailVerificationInProgress) {
        _emailVerificationInProgress = emailVerificationInProgress;
        notifyPropertyChanged(BR.emailVerificationInProgress);
    }

    public VerifyEmailDarkViewModel(Context context, CloudUserService cloudUserService, CloudDeviceService cloudDeviceService,
                                    ToastService toastService, NavigationService navigationService,
                                    String email, String password,
                                    String message, SplashViewModel splashViewModel) {
        _cloudUserService = cloudUserService;
        _cloudDeviceService = cloudDeviceService;
        _toastService = toastService;
        _navigationService = navigationService;
        _context = context;
        _email = email;
        _password = password;
        _splashViewModel = splashViewModel;
        setInstruction(message);
    }

    @NonNull
    private Ticker createVerificationTicker() {
        return new Ticker(new Ticker.Listener() {
            @Override
            public void onTick() {
                checkEmailVerified();
            }

            @Override
            public void onElapsed() {

            }
        }, -1, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        _splashViewModel.setBusy(false);
        _verificationTicker = createVerificationTicker();
    }

    @Override
    public void onPause() {
        super.onPause();
        _verificationTicker.stop();
    }

    @Override
    public FragmentVerifyEmailDarkBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentVerifyEmailDarkBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email_dark, container, false);
        binding.setVm(this);
        return binding;
    }

    private void checkEmailVerified() {
        _splashViewModel.setErrorMessage(null);
        _cloudUserService.refreshUser(new CloudUserService.UserCallback() {
            @Override
            public void success(CloudUser user) {
                if (user.isEmailVerified()) {
                    done(user.getId());
                }
            }

            @Override
            public void failure(CloudException exception) {
                _splashViewModel.setErrorMessage(exception.getLocalizedMessage());
            }
        });

    }

    private void done(final String userId) {
        _verificationTicker.stop();
        _cloudDeviceService.getUserSnapshot(userId, new CloudActionCallback<UserSnapshot>() {
            @Override
            public void success(UserSnapshot data) {
                _navigationService.startMainPresentation(userId, false, data);

            }

            @Override
            public void failure(CloudException exception) {
                _splashViewModel.setErrorMessage(exception.getLocalizedMessage());
            }
        });
    }

    public void resendToken() {
        _splashViewModel.setBusy(true);
        setEmailVerificationInProgress(true);
        _cloudUserService.resendVerificationEmail(_email, _password, new CloudUserService.UserCallback() {
            @Override
            public void success(CloudUser user) {
                _splashViewModel.setBusy(false);
                String emailVerifySuccessMessage = _context.getString(R.string.email_verify_success, user.getEmail());
                _toastService.makeToast(emailVerifySuccessMessage, true);
            }

            @Override
            public void failure(CloudException exception) {
                setEmailVerificationInProgress(false);
                _splashViewModel.setBusy(false);
                String verifyErrorMessage = _context.getString(R.string.error_verifying_email, exception.getLocalizedMessage());
                _splashViewModel.setErrorMessage(verifyErrorMessage);
            }
        });
    }

    public void cancelRegistration() {
        _splashViewModel.setBusy(true);
        _cloudUserService.cancelRegistration(_email, _password, new CloudUserService.UserCallback() {
            @Override
            public void success(CloudUser user) {
                _splashViewModel.setBusy(false);
                _navigationService.goBackTo(SignInViewModel.class);
            }

            @Override
            public void failure(CloudException exception) {
                _splashViewModel.setBusy(false);
                String verifyErrorMessage = _context.getString(R.string.error_cancelling_registration, exception.getLocalizedMessage());
                _splashViewModel.setErrorMessage(verifyErrorMessage);
            }
        });
    }
}