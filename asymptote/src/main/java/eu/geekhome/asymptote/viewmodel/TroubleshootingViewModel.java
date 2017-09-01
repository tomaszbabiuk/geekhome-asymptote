package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.InjectedViewModel;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.viewparams.ShowBackButtonInToolbarViewParam;
import eu.geekhome.asymptote.databinding.FragmentTroubleshootingBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.utils.Ticker;

public class TroubleshootingViewModel extends InjectedViewModel<FragmentTroubleshootingBinding> {

    private HelpActionBarViewModel _actionBarModel;
    private String _errorMessage;
    private ObservableArrayList<LayoutHolder> _signals;
    private SignalItemViewModel.Type _selectedSignalType;
    private int _blinkerPos;
    private Ticker _blinker;

    @Inject
    NavigationService _navigationService;
    @Inject
    Context _context;

    @NonNull
    private Ticker createBlinker() {
        _blinkerPos = 0;

        return new Ticker(new Ticker.Listener() {
            @Override
            public void onTick() {
                if (_signals != null) {
                    for (LayoutHolder holder : _signals) {
                        ((SignalItemViewModel) holder).blink(_blinkerPos);
                    }
                    _blinkerPos++;
                }
            }

            @Override
            public void onElapsed() {
            }
        }, -1, 400);
    }

    @Bindable
    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        notifyPropertyChanged(BR.errorMessage);
        _errorMessage = errorMessage;
    }

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    public TroubleshootingViewModel(ActivityComponent activityComponent) {
        super(activityComponent);
        _actionBarModel = new HelpActionBarViewModel(activityComponent);
        _signals = createSignals();
        setSelectedSignalType(SignalItemViewModel.Type.OneBlink);
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    private ObservableArrayList<LayoutHolder> createSignals() {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();

        SignalItemViewModel oneBlink = new SignalItemViewModel(this, SignalItemViewModel.Type.OneBlink);
        SignalItemViewModel twoBlinks = new SignalItemViewModel(this, SignalItemViewModel.Type.TwoBlinks);
        SignalItemViewModel threeBlinks = new SignalItemViewModel(this, SignalItemViewModel.Type.ThreeBlinks);
        SignalItemViewModel fourBlinks = new SignalItemViewModel(this, SignalItemViewModel.Type.FourBlinks);
        SignalItemViewModel turnedOn = new SignalItemViewModel(this, SignalItemViewModel.Type.TurnedOn);
        SignalItemViewModel blinking = new SignalItemViewModel(this, SignalItemViewModel.Type.Blinking);
        SignalItemViewModel none = new SignalItemViewModel(this, SignalItemViewModel.Type.None);

        result.add(oneBlink);
        result.add(twoBlinks);
        result.add(threeBlinks);
        result.add(fourBlinks);
        result.add(blinking);
        result.add(turnedOn);
        result.add(none);

        return result;
    }

    @Override
    public FragmentTroubleshootingBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentTroubleshootingBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_troubleshooting, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    public void onResume() {
        super.onResume();
        _blinker = createBlinker();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (_blinker != null) {
            _blinker.stop();
        }
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getSignals() {
        return _signals;
    }

    public void setSelectedSignalType(SignalItemViewModel.Type selectedSignalType) {
        if (_signals != null) {
            for (LayoutHolder holderItem : _signals) {
                SignalItemViewModel item = (SignalItemViewModel) holderItem;
                item.setSelected(item.getSignalType() == selectedSignalType);
                _blinkerPos = 0;
            }
        }
        _selectedSignalType = selectedSignalType;
    }

    public void onNext() {
        String description = _context.getString(_selectedSignalType.getDescriptionResId());
        ObservableArrayList<LayoutHolder> sections = new ObservableArrayList<>();
        sections.add(new HeaderItemViewModel(HeaderItemViewModel.Style.Main, description));
        switch (_selectedSignalType) {
            case None:
                addSection(R.plurals.solutions, R.array.signal_none_solutions, sections);
                break;
            case Blinking:
                addSection(R.plurals.solutions, R.array.signal_blinking_solutions, sections);
                break;
            case OneBlink:
                addSection(R.plurals.solutions, R.array.signal_1blink_solutions, sections);
                addSection(R.plurals.reset_procedure_steps, R.array.reset_procedure, sections);
                break;
            case TwoBlinks:
                addSection(R.plurals.solutions, R.array.signal_2blinks_solutions, sections);
                break;
            case ThreeBlinks:
                addSection(R.plurals.solutions, R.array.signal_3blinks_solutions, sections);
                break;
            case FourBlinks:
                addSection(R.plurals.solutions, R.array.signal_4blinks_solutions, sections);
                break;
            case TurnedOn:
                addSection(R.plurals.solutions, R.array.signal_turned_on_solutions, sections);
                break;
        }
        CMSViewModel repairDetailsModel = new CMSViewModel(getActivityComponent(), sections);
        _navigationService.showViewModel(repairDetailsModel, new ShowBackButtonInToolbarViewParam());
    }

    private void addSection(@PluralsRes int titleResId, @ArrayRes int itemsResId, ObservableArrayList<LayoutHolder> targetList) {
        String[] itemsContent = _context.getResources().getStringArray(itemsResId);
        String title = _context.getResources().getQuantityString(titleResId, itemsContent.length, itemsContent.length);
        LayoutHolder headerItem = new HeaderItemViewModel(HeaderItemViewModel.Style.Sub, title);
        targetList.add(headerItem);
        for (int i = 0; i < itemsContent.length; i++) {
            LayoutHolder contentItem = new OrderedItemViewModel(i + 1, itemsContent[i]);
            targetList.add(contentItem);
        }

    }
}