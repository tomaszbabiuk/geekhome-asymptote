package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentVerifyEmailLightBinding;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.utils.Ticker;

public class VerifyEmailLightViewModel extends ViewModel<FragmentVerifyEmailLightBinding> {
    private final HelpActionBarViewModel _actionBarModel;
    private String _instruction;
    private Ticker _verificationTicker;
    private String _errorMessage;

    private final CloudUserService _cloudUserService;
    private final NavigationService _navigationService;
    private final Context _context;

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
        notifyPropertyChanged(eu.geekhome.asymptote.BR.errorMessage);
    }

    @Bindable
    public String getInstruction() {
        return _instruction;
    }

    public void setInstruction(String instruction) {
        _instruction = instruction;
        notifyPropertyChanged(BR.instruction);
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    public VerifyEmailLightViewModel(Context context, MainViewModelsFactory factory, NavigationService navigationService,
                                     CloudUserService cloudUserService, String email) {
        _context = context;
        _actionBarModel = factory.createHelpActionBarModel();
        _navigationService = navigationService;
        _cloudUserService = cloudUserService;

        String instructionMessage = _context.getString(R.string.email_verify_success, email);
        setInstruction(instructionMessage);

        _verificationTicker = createVerificationTicker();
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
        _verificationTicker = createVerificationTicker();
    }

    @Override
    public void onPause() {
        super.onPause();
        _verificationTicker.stop();
    }

    @Override
    public FragmentVerifyEmailLightBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentVerifyEmailLightBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_verify_email_light, container, false);
        binding.setVm(this);
        Animation rotateAnimation = AnimationUtils.loadAnimation(_context, R.anim.rotate_around_center_point_linear);
        binding.gearImage.startAnimation(rotateAnimation);
        return binding;
    }

    private void checkEmailVerified() {
        setErrorMessage(null);
        _cloudUserService.refreshUser(new CloudUserService.UserCallback() {
            @Override
            public void success(CloudUser user) {
                if (user.isEmailVerified()) {
                    done();
                }
            }

            @Override
            public void failure(CloudException exception) {
                setErrorMessage(exception.getLocalizedMessage());
            }
        });

    }

    public void done() {
        _navigationService.goBackTo(ProfileViewModel.class);
    }
}