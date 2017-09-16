package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.ObservableArrayList;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.BoardRole;

class RoleCreator {

    static ObservableArrayList<LayoutHolder> generateRoles(Context context, BoardId boardId, EditSensorViewModel editModel) {
        switch (boardId) {
            case SonoffTOUCH:
                return createRolesForSonoffTouch(context, editModel);
            case SonoffTH_AM2301:
                return createRolesForSonoffTH_AM2301(context, editModel);
            case SonoffBASIC:
            case SonoffTH_None:
                return createRolesForSonoffTHNoneOrBasic(context, editModel);
            case SonoffTH_DS18B20:
                return createRolesForSonoffTH_DS18B20(context, editModel);
            case H801 :
                return createRolesForH801(context, editModel);
            case H802 :
            case ElectrodragonLed:
                return createRolesForH802(context, editModel);
            case SonoffSC:
                return createRolesForSonoffSC(context, editModel);
            case Sonoff4CH:
                return createRolesForSonoff4CH(context, editModel);
            case SonoffDUAL:
                return createRolesForSonoffDual(context, editModel);
            case geekGATE:
                return createRolesForGeekGate(context, editModel);
            case Elecrodragon2REL:
                return createRolesForElectrodragon2REL(context, editModel);
            default :
                return new ObservableArrayList<>();
        }
    }

    private static ObservableArrayList<LayoutHolder> createRolesForGeekGate(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel gate = new RoleItemViewModel(editModel,
                BoardRole.GATE,
                context.getString(R.string.role_gate),
                context.getString(R.string.role_gate_desc));
        roles.add(gate);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForElectrodragon2REL(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel mainsRole = new RoleItemViewModel(editModel,
                BoardRole.MAINS2,
                context.getString(R.string.role_mains_switch),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS2_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
        roles.add(mainsRole);
        roles.add(mainsRoleAdvanced);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoffDual(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel mainsRole = new RoleItemViewModel(editModel,
                BoardRole.MAINS2,
                context.getString(R.string.role_mains_switch),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS2_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
        roles.add(mainsRole);
        roles.add(mainsRoleAdvanced);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoff4CH(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel mainsRole = new RoleItemViewModel(editModel,
                BoardRole.MAINS4,
                context.getString(R.string.role_mains_switch),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS4_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
//        RoleItemViewModel slave = new RoleItemViewModel(editModel,
//                BoardRole.GeekHOME,
//                context.getString(R.string.role_geekhome),
//                context.getResources().getQuantityString(R.plurals.digital_output_ports, 1, 1));
        roles.add(mainsRole);
        roles.add(mainsRoleAdvanced);
        //roles.add(slave);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoffSC(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel multisensorRole = new RoleItemViewModel(editModel,
                BoardRole.MULTISENSOR,
                context.getString(R.string.role_multisensor),
                context.getString(R.string.role_multisensor_desc));

        String multiPortsDesc =
                context.getResources().getQuantityString(R.plurals.temperature_ports, 1, 1) +
                        "\r\n" +
                        context.getResources().getQuantityString(R.plurals.humidity_ports, 1, 1) +
                        "\r\n" +
                        context.getResources().getQuantityString(R.plurals.luminosity_ports, 1, 1) +
                        "\r\n" +
                        context.getResources().getQuantityString(R.plurals.noise_ports, 1, 1) +
                        "\r\n" +
                        context.getResources().getQuantityString(R.plurals.noise_ports, 1, 1);
//        RoleItemViewModel slave = new RoleItemViewModel(editModel,
//                BoardRole.GeekHOME,
//                context.getString(R.string.role_geekhome),
//                multiPortsDesc);
        roles.add(multisensorRole);
        //roles.add(slave);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoffTH_DS18B20(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel mainsRelayRole = new RoleItemViewModel(editModel,
                BoardRole.MAINS1,
                context.getString(R.string.role_mains_switch),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS1_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel heatingThermostatRole = new RoleItemViewModel(editModel,
                BoardRole.HEATING_THERMOSTAT,
                context.getString(R.string.role_heating_thermostat),
                context.getString(R.string.role_heating_thermostat_desc));
        RoleItemViewModel coolingThermostatRole = new RoleItemViewModel(editModel,
                BoardRole.COOLING_THERMOSTAT,
                context.getString(R.string.role_cooling_thermostat),
                context.getString(R.string.role_cooling_thermostat_desc));
//        RoleItemViewModel slave = new RoleItemViewModel(editModel,
//                BoardRole.GeekHOME,
//                context.getString(R.string.role_geekhome),
//                context.getResources().getQuantityString(R.plurals.digital_output_ports, 1, 1));
        roles.add(mainsRelayRole);
        roles.add(mainsRoleAdvanced);
        roles.add(heatingThermostatRole);
        roles.add(coolingThermostatRole);
        //roles.add(slave);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoffTH_AM2301(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel mainsRelayRole = new RoleItemViewModel(editModel,
                BoardRole.MAINS1,
                context.getString(R.string.role_mains_switch),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS1_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel heatingThermostatRole = new RoleItemViewModel(editModel,
                BoardRole.HEATING_THERMOSTAT,
                context.getString(R.string.role_heating_thermostat),
                context.getString(R.string.role_heating_thermostat_desc));
        RoleItemViewModel coolingThermostatRole = new RoleItemViewModel(editModel,
                BoardRole.COOLING_THERMOSTAT,
                context.getString(R.string.role_cooling_thermostat),
                context.getString(R.string.role_cooling_thermostat_desc));
        RoleItemViewModel humidificationHygrostatRole = new RoleItemViewModel(editModel,
                BoardRole.HUMIDIFICATION_HYGROSTAT,
                context.getString(R.string.role_humidification_hygrostat),
                context.getString(R.string.role_humidification_hygrostat_desc));
        RoleItemViewModel dryingHygrostatRole = new RoleItemViewModel(editModel,
                BoardRole.DRYING_HYGROSTAT,
                context.getString(R.string.role_drying_hygrostat),
                context.getString(R.string.role_drying_hygrostat_desc));
//        RoleItemViewModel slave = new RoleItemViewModel(editModel,
//                BoardRole.GeekHOME,
//                context.getString(R.string.role_geekhome),
//                context.getResources().getQuantityString(R.plurals.digital_output_ports, 1, 1));
        roles.add(mainsRelayRole);
        roles.add(mainsRoleAdvanced);
        roles.add(heatingThermostatRole);
        roles.add(coolingThermostatRole);
        roles.add(dryingHygrostatRole);
        roles.add(humidificationHygrostatRole);
        //roles.add(slave);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoffTHNoneOrBasic(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel mainsRole = new RoleItemViewModel(editModel,
                BoardRole.MAINS1,
                context.getString(R.string.role_mains_switch),
                context.getString(R.string.role_mains_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS1_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
        roles.add(mainsRole);
        roles.add(mainsRoleAdvanced);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForSonoffTouch(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel oneRelayRole = new RoleItemViewModel(editModel,
                BoardRole.TOUCH1,
                context.getString(R.string.role_light_switch),
                context.getString(R.string.role_light_switch_desc));
        RoleItemViewModel mainsRoleAdvanced = new RoleItemViewModel(editModel,
                BoardRole.MAINS1_ADV,
                context.getString(R.string.role_mains_switch_advanced),
                context.getString(R.string.role_mains_desc));
        roles.add(oneRelayRole);
        roles.add(mainsRoleAdvanced);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForH801(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel rgbRole = new RoleItemViewModel(editModel,
                BoardRole.RGB_2PWM,
                context.getString(R.string.role_rgb_2pwm_controller),
                context.getString(R.string.role_rgb_2pwm_controller_desc));
        RoleItemViewModel dummyRole2 = new RoleItemViewModel(editModel,
                BoardRole.MULTI_PWM,
                context.getString(R.string.role_pwm_multichannel),
                context.getString(R.string.role_pwm_multichannel_desc));
//        RoleItemViewModel slave = new RoleItemViewModel(editModel,
//                BoardRole.GeekHOME,
//                context.getString(R.string.role_geekhome),
//                context.getResources().getQuantityString(R.plurals.channels_pwm, 5, 5));
        roles.add(rgbRole);
        roles.add(dummyRole2);
        //roles.add(slave);

        return roles;
    }

    private static ObservableArrayList<LayoutHolder> createRolesForH802(Context context, EditSensorViewModel editModel) {
        ObservableArrayList<LayoutHolder> roles = new ObservableArrayList<>();
        RoleItemViewModel rgbwRole = new RoleItemViewModel(editModel,
                BoardRole.RGBW,
                context.getString(R.string.role_rgbw_controller),
                context.getString(R.string.role_rgbw_controller_desc));
        RoleItemViewModel rgbRole = new RoleItemViewModel(editModel,
                BoardRole.RGB_1PWM,
                context.getString(R.string.role_rgb_1pwm_controller),
                context.getString(R.string.role_rgb_1pwm_controller_desc));
        RoleItemViewModel multichannelPWM = new RoleItemViewModel(editModel,
                BoardRole.MULTI_PWM,
                context.getString(R.string.role_pwm_multichannel),
                context.getString(R.string.role_pwm_multichannel_desc));
//        RoleItemViewModel slave = new RoleItemViewModel(editModel,
//                BoardRole.GeekHOME,
//                context.getString(R.string.role_geekhome),
//                context.getResources().getQuantityString(R.plurals.channels_pwm, 4, 4));
        roles.add(rgbwRole);
        roles.add(rgbRole);
        roles.add(multichannelPWM);
        //roles.add(slave);

        return roles;
    }
}
