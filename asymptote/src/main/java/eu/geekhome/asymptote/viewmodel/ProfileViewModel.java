package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentProfileBinding;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class ProfileViewModel extends ViewModel<FragmentProfileBinding> {
    private final HelpActionBarViewModel _actionBarModel;
    private CloudUser _user;
    private String _errorMessage;

    private final CloudUserService _cloudUserService;
    private final NavigationService _navigationService;
    private final MainViewModelsFactory _factory;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public CloudUser getUser() {
        return _user;
    }

    public void setUser(CloudUser value) {
        _user = value;
        notifyPropertyChanged(BR.user);
    }

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        _errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }

    public ProfileViewModel(MainViewModelsFactory factory, NavigationService navigationService, CloudUserService cloudUserService) {
        _factory = factory;
        _navigationService = navigationService;
        _cloudUserService = cloudUserService;
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        _cloudUserService.refreshUser(new CloudUserService.UserCallback() {
            @Override
            public void success(CloudUser user) {
                setUser(user);
            }

            @Override
            public void failure(CloudException exception) {
                setErrorMessage(exception.getLocalizedMessage());
            }
        });
    }

    @Override
    public FragmentProfileBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentProfileBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        binding.setVm(this);
        return binding;
    }

    public void changePassword() {
        setErrorMessage(null);
        ChangePasswordViewModel changePasswordViewModel = _factory.createChangePasswordViewModel();
        _navigationService.showViewModel(changePasswordViewModel, new ShowBackButtonInToolbarViewParam());
    }

    public void changeEmail() {
        setErrorMessage(null);
        ChangeEmailViewModel changeEmailViewModel = _factory.createChangeEmailViewModel();
        _navigationService.showViewModel(changeEmailViewModel, new ShowBackButtonInToolbarViewParam());
    }

    public void logout() {
        _cloudUserService.signOut();
        _navigationService.endMainPresentation();
    }
}