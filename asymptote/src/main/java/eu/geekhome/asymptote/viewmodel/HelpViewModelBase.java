package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

abstract class HelpViewModelBase<T extends ViewDataBinding> extends WiFiAwareViewModel<T> {
    private HelpActionBarViewModel _actionBarModel;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    HelpViewModelBase(MainViewModelsFactory factory, WiFiHelper wifiHelper, NavigationService navigationService) {
        super(factory, wifiHelper, navigationService);
        _actionBarModel = factory.createHelpActionBarModel();
    }

    @Override
    protected boolean isCloudOnlyAllowed() {
        return false;
    }
}