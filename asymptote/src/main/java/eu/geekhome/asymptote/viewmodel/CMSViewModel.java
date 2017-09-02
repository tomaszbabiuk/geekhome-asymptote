package eu.geekhome.asymptote.viewmodel;

import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentCmsBinding;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class CMSViewModel extends ViewModel<FragmentCmsBinding> {
    private HelpActionBarViewModel _actionBarModel;
    private final ObservableArrayList<LayoutHolder> _sections;

    private final NavigationService _navigationService;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    public CMSViewModel(MainViewModelsFactory factory, NavigationService navigationService, ObservableArrayList<LayoutHolder> sections) {
        _navigationService = navigationService;
        _actionBarModel = factory.createHelpActionBarModel();
        _sections = sections;
    }

    @Override
    public FragmentCmsBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentCmsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cms, container, false);
        binding.setVm(this);
        return binding;
    }

    public void onOk() {
        _navigationService.goBackTo(MainViewModel.class);
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getSections() {
        return _sections;
    }
}
