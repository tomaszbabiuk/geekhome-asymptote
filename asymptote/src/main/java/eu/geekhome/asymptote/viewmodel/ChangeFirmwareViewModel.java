package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.bindingutils.ViewModel;
import eu.geekhome.asymptote.databinding.FragmentChangeFirmwareBinding;
import eu.geekhome.asymptote.model.FirmwareSet;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;


public class ChangeFirmwareViewModel extends ViewModel<FragmentChangeFirmwareBinding> {

    private ObservableArrayList<LayoutHolder> _firmwares;
    private HelpActionBarViewModel _actionBarModel;
    private final SensorItemViewModel _sensor;

    private final FirmwareRepository _firmwareRepository;
    private final EmergencyManager _emergencyManager;
    private final Context _context;
    private final MainViewModelsFactory _factory;

    @Bindable
    public HelpActionBarViewModel getActionBarModel() {
        return _actionBarModel;
    }

    @Bindable
    public SensorItemViewModel getSensor() {
        return _sensor;
    }

    public ChangeFirmwareViewModel(Context context, MainViewModelsFactory factory,
                                   FirmwareRepository firmwareRepository, EmergencyManager emergencyManager,
                                   SensorItemViewModel sensor) {
        _context = context;
        _factory = factory;
        _firmwareRepository = firmwareRepository;
        _emergencyManager = emergencyManager;
        _sensor = sensor;
        _firmwares = createFirmwares();
        _actionBarModel = _factory.createHelpActionBarModel();
    }

    private ObservableArrayList<LayoutHolder> createFirmwares() {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();
        FirmwareSet set = _firmwareRepository.findFirmwareSet(_sensor.getSyncData().getDeviceKey().getBoardId());

        boolean isLatest = _firmwareRepository.isLatest(_sensor.getSyncData().getSystemInfo());

        if (set != null) {
            FirmwareItemViewModel.Context wifiFirmwareContext =
                    _sensor.getSyncData().getSystemInfo().getVariant() != Variant.WiFi ? FirmwareItemViewModel.Context.Change :
                            isLatest ? FirmwareItemViewModel.Context.Actual : FirmwareItemViewModel.Context.Update;
            FirmwareItemViewModel wifiItem = _factory.createFirmwareItemViewModel(set.getWifiFirmware(),
                    _sensor, "LAN", _context.getString(R.string.wifi_firmware_desc),
                    wifiFirmwareContext, true);

            FirmwareItemViewModel.Context firebaseFirmwareContext =
                    _sensor.getSyncData().getSystemInfo().getVariant() != Variant.Firebase ? FirmwareItemViewModel.Context.Change :
                            isLatest ? FirmwareItemViewModel.Context.Actual : FirmwareItemViewModel.Context.Update;
            FirmwareItemViewModel firebaseItem = _factory.createFirmwareItemViewModel(set.getFirebaseFirmware(),
                    _sensor, "IoT", _context.getString(R.string.iot_firmware_desc),
                    firebaseFirmwareContext, !_emergencyManager.isEmergency());

            FirmwareItemViewModel.Context hybridFirmwareContext =
                    _sensor.getSyncData().getSystemInfo().getVariant() != Variant.Hybrid ? FirmwareItemViewModel.Context.Change :
                            isLatest ? FirmwareItemViewModel.Context.Actual : FirmwareItemViewModel.Context.Update;
            FirmwareItemViewModel hybridItem = _factory.createFirmwareItemViewModel( set.getHybridFirmware(),
                    _sensor, "LAN+IoT", _context.getString(R.string.hybrid_firmware_desc),
                    hybridFirmwareContext, !_emergencyManager.isEmergency());


            switch (_sensor.getSyncData().getSystemInfo().getVariant()) {
                case WiFi:
                    wifiItem.setSelected(true);
                    result.add(wifiItem);
                    result.add(firebaseItem);
                    result.add(hybridItem);
                    break;
                case Firebase:
                    firebaseItem.setSelected(true);
                    result.add(firebaseItem);
                    result.add(wifiItem);
                    result.add(hybridItem);
                    break;
                case Hybrid:
                    hybridItem.setSelected(true);
                    result.add(hybridItem);
                    result.add(firebaseItem);
                    result.add(wifiItem);
                    break;
            }
        }

        return result;
    }

    @Override
    public FragmentChangeFirmwareBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentChangeFirmwareBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_firmware, container, false);
        binding.setVm(this);
        return binding;
    }

    @Bindable
    public ObservableArrayList<LayoutHolder> getFirmwares() {
        return _firmwares;
    }
}
