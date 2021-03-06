package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityScope;
import eu.geekhome.asymptote.model.BoardRole;
import eu.geekhome.asymptote.model.DeviceSyncData;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.FavoriteColorsService;
import eu.geekhome.asymptote.services.GeneralDialogService;

@ActivityScope
public class ControlsCreator {

    private final Context _context;
    private final FavoriteColorsService _favoriteColorsService;
    private final ColorDialogService _colorDialogService;
    private final GeneralDialogService _generalDialogService;

    @Inject
    public ControlsCreator(Context context, FavoriteColorsService favoriteColorsService,
                           ColorDialogService colorDialogService, GeneralDialogService generalDialogService) {
        _context = context;
        _favoriteColorsService = favoriteColorsService;
        _colorDialogService = colorDialogService;
        _generalDialogService = generalDialogService;
    }

    ObservableArrayList<LayoutHolder> createControls(DeviceSyncData syncData, SensorItemViewModel sensorModel) {
        ObservableArrayList<LayoutHolder> result = new ObservableArrayList<>();
        if (syncData.getRole() != BoardRole.GeekHOME) {
//            if (syncData.getRole() == BoardRole.LIGHT_SWITCH_TRADITIONAL) {
//                for (int i = 0; i < syncData.getRelayStates().length; i++) {
//                    ControlRelayItemViewModel relayModel = new ControlRelayItemViewModel(sensorModel, _context, i,
//                            syncData.getRelayStates()[i]);
//                    result.add(relayModel);
//                }
//            }
            if (syncData.getRole() == BoardRole.HEATING_THERMOSTAT) {
                String activeMessage = _context.getString(R.string.heating_on);
                String inactiveMessage = _context.getString(R.string.heating_off);
                result.add(new ControlThermostatItemViewModel(sensorModel, _context, 0, syncData, activeMessage, inactiveMessage));
            }

            if (syncData.getRole() == BoardRole.COOLING_THERMOSTAT) {
                String activeMessage = _context.getString(R.string.cooling_on);
                String inactiveMessage = _context.getString(R.string.cooling_off);
                result.add(new ControlThermostatItemViewModel(sensorModel, _context, 0, syncData, activeMessage, inactiveMessage));
            }

            if (syncData.getRole() == BoardRole.DRYING_HYGROSTAT) {
                String activeMessage = _context.getString(R.string.drying_on);
                String inactiveMessage = _context.getString(R.string.drying_off);
                result.add(new ControlHygrostatItemViewModel(sensorModel, _context, 0, syncData, activeMessage, inactiveMessage));
            }

            if (syncData.getRole() == BoardRole.HUMIDIFICATION_HYGROSTAT) {
                String activeMessage = _context.getString(R.string.humidification_on);
                String inactiveMessage = _context.getString(R.string.humidification_off);
                result.add(new ControlHygrostatItemViewModel(sensorModel, _context, 0, syncData, activeMessage, inactiveMessage));
            }

            if (syncData.getRole() == BoardRole.GATE) {
                String state = syncData.getState();
                result.add(new ControlGateItemViewModel(sensorModel, _context, state));
            }

            if (syncData.getTemperature() != null || syncData.getHumidity() != null) {
                result.add(new ControlTempAndHumItemViewModel(sensorModel));
            }

            if (syncData.getLuminosity() != null || syncData.getNoise() != null) {
                result.add(new ControlLumAndNoiseItemViewModel(sensorModel));
            }

            if (syncData.getNoise() != null) {
                result.add(new ControlDustItemViewModel(sensorModel));
            }

            if (!syncData.getRole().isAutomatic()) {
                if (syncData.isSensor()) {
                    result.add(new ControlSeparatorViewModel());
                }

                if (syncData.getRelayStates() != null) {
                    if (syncData.getRelayStates().length > 0) {
                        for (int i = 0; i < syncData.getRelayStates().length; i++) {
                            if (syncData.getRole().isAdvanced()) {
                                long impulseSetting = syncData.getParams()[i * 2];
                                long impulseNow = syncData.getRelayImpulses()[i];
                                boolean relayState = syncData.getRelayStates()[i];

                                ControlImpulseItemViewModel impulseModel = new ControlImpulseItemViewModel(sensorModel, _context, i,
                                        relayState, impulseSetting, impulseNow);
                                result.add(impulseModel);
                            } else {
                                ControlRelayItemViewModel relayModel = new ControlRelayItemViewModel(sensorModel, _context, i,
                                        syncData.getRelayStates()[i]);
                                result.add(relayModel);
                            }
                        }
                    }
                }

                if (syncData.getRole() == BoardRole.MULTI_PWM) {
                    String[] names = {"R", "G", "B", "W", ""};

                    if (syncData.getPwmDuties().length == 5) {
                        names[3] = "W1";
                        names[4] = "W2";
                    }

                    if (syncData.getPwmDuties() != null) {
                        if (syncData.getPwmDuties().length > 0) {
                            for (int i = 0; i < syncData.getPwmDuties().length; i++) {
                                ControlPWMItemViewModel pwmModel = new ControlPWMItemViewModel(sensorModel, _context, i,
                                        syncData.getPwmDuties()[i], names[i]);
                                result.add(pwmModel);
                            }
                        }
                    }
                }

                if (syncData.getRole() == BoardRole.RGB_1PWM || syncData.getRole() == BoardRole.RGB_2PWM) {
                    String[] names = {"R", "G", "B", "W", ""};

                    if (syncData.getPwmDuties().length == 5) {
                        names[3] = "W1";
                        names[4] = "W2";
                    }

                    if (syncData.getPwmDuties().length > 0) {
                        ControlRGBItemViewModel rgbModel = new ControlRGBItemViewModel(sensorModel, _colorDialogService,
                                _favoriteColorsService, _generalDialogService, 0, 1, 2, -1,
                                syncData.getPwmDuties()[0], syncData.getPwmDuties()[1], syncData.getPwmDuties()[2]);
                        result.add(rgbModel);
                    }

                    ControlPWMItemViewModel pwmW1Model = new ControlPWMItemViewModel(sensorModel, _context, 3,
                            syncData.getPwmDuties()[3], names[3]);
                    result.add(pwmW1Model);

                    if (syncData.getRole() == BoardRole.RGB_2PWM) {
                        ControlPWMItemViewModel pwmW2Model = new ControlPWMItemViewModel(sensorModel, _context, 4,
                                syncData.getPwmDuties()[4], names[5]);
                        result.add(pwmW2Model);
                    }
                }

                if (syncData.getRole() == BoardRole.RGBW) {
                    if (syncData.getPwmDuties().length > 0) {
                        ControlRGBItemViewModel rgbModel = new ControlRGBItemViewModel(sensorModel, _colorDialogService,
                                _favoriteColorsService, _generalDialogService, 0, 1, 2, 3,
                                syncData.getPwmDuties()[0], syncData.getPwmDuties()[1], syncData.getPwmDuties()[2]);
                        result.add(rgbModel);
                    }
                }
            } else if (syncData.getRelayStates().length > 1) {
                result.add(new ControlSeparatorViewModel());

                for (int i = 1; i < syncData.getRelayStates().length; i++) {
                    ControlRelayItemViewModel relayModel = new ControlRelayItemViewModel(sensorModel, _context, i,
                            syncData.getRelayStates()[i]);
                    result.add(relayModel);
                }
            }
        }

        return result;
    }

}
