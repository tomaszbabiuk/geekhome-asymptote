package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.databinding.library.baseAdapters.BR;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentLoginWithEmailBinding;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.SplashViewModelsFactory;
import eu.geekhome.asymptote.utils.KeyboardHelper;
import eu.geekhome.asymptote.validation.ValidationContext;

public class LoginViewModel extends ViewModel<FragmentLoginWithEmailBinding> {
    private final ValidationContext _validation = new ValidationContext();
    private final CloudUserService _cloudUserService;
    private final Context _context;
    private final NavigationService _navigationService;
    private final CloudDeviceService _cloudDeviceService;
    private final SplashViewModelsFactory _factory;
    private SplashViewModel _splashViewModel;
    private String _email;
    private String _password;
    private View _rootView;

    @Bindable
    public ValidationContext getValidation() {
        return _validation;
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
    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
        notifyPropertyChanged(BR.password);
    }

    public LoginViewModel(SplashViewModelsFactory factory, Context context, NavigationService navigationService,
                          CloudUserService cloudUserService, CloudDeviceService cloudDeviceService , SplashViewModel splashViewModel) {
        _factory = factory;
        _context = context;
        _navigationService = navigationService;
        _cloudUserService = cloudUserService;
        _cloudDeviceService = cloudDeviceService;
        _splashViewModel = splashViewModel;
    }

    @Override
    public FragmentLoginWithEmailBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentLoginWithEmailBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_with_email, container, false);
        binding.setVm(this);
        _rootView = binding.getRoot();
        return binding;
    }

    @Override
    public void onResume() {
        super.onResume();
        setEmail(null);
        setPassword(null);
    }

    public void signInWithEmail() {
        KeyboardHelper.hideKeyboard(_rootView);
        if (_validation.validate()) {
            _splashViewModel.setBusy(true);
            _splashViewModel.setErrorMessage(null);

            _cloudUserService.signInWithEmailAndPassword(getEmail(), getPassword(), new CloudUserService.UserCallback() {
                @Override
                public void success(CloudUser user) {
                    if (!user.isEmailVerified()) {
                        _splashViewModel.setBusy(false);
                        String verificationMessage = _context.getString(R.string.finish_registration_process, getEmail());

                        VerifyEmailDarkViewModel verifyViewModel = _factory.createVerifyEmailDarkViewModel(getEmail(),
                                getPassword(), verificationMessage, _splashViewModel);
                        _navigationService.showViewModel(verifyViewModel);
                    } else {
                        loadUserSnapshotAndProceed(user);
                    }
                }

                @Override
                public void failure(CloudException exception) {
                    showError(exception);
                }
            });
        }
    }

    private void loadUserSnapshotAndProceed(final CloudUser user) {
        _cloudDeviceService.getUserSnapshot(user.getId(), new CloudActionCallback<UserSnapshot>() {
            @Override
            public void success(UserSnapshot data) {
                _navigationService.startMainPresentation(user.getId(), false, data);
            }

            @Override
            public void failure(CloudException exception) {
                showError(exception);
            }
        });
    }

    private void showError(CloudException exception) {
        _splashViewModel.setBusy(false);
        String failureMessage = _context.getString(R.string.unable_to_signin, exception.getLocalizedMessage());
        _splashViewModel.setErrorMessage(failureMessage);
    }
}