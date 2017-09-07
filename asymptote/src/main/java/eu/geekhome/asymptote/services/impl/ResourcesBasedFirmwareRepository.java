package eu.geekhome.asymptote.services.impl;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.FirmwareSet;
import eu.geekhome.asymptote.model.Variant;
public class ResourcesBasedFirmwareRepository extends ResourcesBasedFirmwareRepositoryBase {
    public ResourcesBasedFirmwareRepository() {
        Firmware sonoffTHWifi = new Firmware(Variant.WiFi, "sonoffth/firmware_wifi.bin", "71c245da7d57ef56a32bdd8147fd893e", 1, 4);
        Firmware sonoffTHFirebase = new Firmware(Variant.Firebase, "sonoffth/firmware_firebase.bin", "78eb00b6db88c21be74102c84d1e0256", 1, 4);
        Firmware sonoffTHHybrid = new Firmware(Variant.Hybrid, "sonoffth/firmware_hybrid.bin", "42ff5bda2b455a74b9ad8aad98073bc0", 1, 4);
        FirmwareSet sonoffTHSet = new FirmwareSet(sonoffTHWifi, sonoffTHFirebase, sonoffTHHybrid);
        put(BoardId.SonoffTH_None, sonoffTHSet);
        put(BoardId.SonoffTH_AM2301, sonoffTHSet);
        put(BoardId.SonoffTH_DS18B20, sonoffTHSet);
        Firmware sonoffSCWifi = new Firmware(Variant.WiFi, "sonoffsc/firmware_wifi.bin", "b4d668f71f053ab991bb41a4ec5b22a7", 1, 4);
        Firmware sonoffSCFirebase = new Firmware(Variant.Firebase, "sonoffsc/firmware_firebase.bin", "e94313e33d8e1079dee182fcb46af994", 1, 4);
        Firmware sonoffSCHybrid = new Firmware(Variant.Hybrid, "sonoffsc/firmware_hybrid.bin", "071f221f020cf8795b96c5891d551451", 1, 4);
        FirmwareSet sonoffSCSet = new FirmwareSet(sonoffSCWifi, sonoffSCFirebase, sonoffSCHybrid);
        put(BoardId.SonoffSC, sonoffSCSet);
        Firmware sonoffTouchWifi = new Firmware(Variant.WiFi, "sonofftouch/firmware_wifi.bin", "d226fa111c6f05f5d7c14e6150fa8e17", 1, 4);
        Firmware sonoffTouchFirebase = new Firmware(Variant.Firebase, "sonofftouch/firmware_firebase.bin", "dafd35494685951983b56e448dd2f1d0", 1, 4);
        Firmware sonoffTouchHybrid = new Firmware(Variant.Hybrid, "sonofftouch/firmware_hybrid.bin", "e1509997221ff995055861bd9f325e9b", 1, 4);
        FirmwareSet sonoffTouchSet = new FirmwareSet(sonoffTouchWifi, sonoffTouchFirebase, sonoffTouchHybrid);
        put(BoardId.SonoffTOUCH, sonoffTouchSet);
        Firmware sonoffDualWifi = new Firmware(Variant.WiFi, "sonoffdual/firmware_wifi.bin", "44fc238a589eabf5811dcff58857dc05", 1, 4);
        Firmware sonoffDualFirebase = new Firmware(Variant.Firebase, "sonoffdual/firmware_firebase.bin", "9389767f88967ee78f0bc7c50fd1531b", 1, 4);
        Firmware sonoffDualHybrid = new Firmware(Variant.Hybrid, "sonoffdual/firmware_hybrid.bin", "e203d469c478995d17cec353a0c1487f", 1, 4);
        FirmwareSet sonoffDualSet = new FirmwareSet(sonoffDualWifi, sonoffDualFirebase, sonoffDualHybrid);
        put(BoardId.SonoffDUAL, sonoffDualSet);
        Firmware sonoff4ChWifi = new Firmware(Variant.WiFi, "sonoff4ch/firmware_wifi.bin", "b3ea857a4d9078c0eb4119bdc9470e23", 1, 4);
        Firmware sonoff4ChFirebase = new Firmware(Variant.Firebase, "sonoff4ch/firmware_firebase.bin", "9b621aa2768c54745bedb5d99f3d7d05", 1, 4);
        Firmware sonoff4ChHybrid = new Firmware(Variant.Hybrid, "sonoff4ch/firmware_hybrid.bin", "b30cdc4c41d180ee66e5cd8651360645", 1, 4);
        FirmwareSet sonoff4ChSet = new FirmwareSet(sonoff4ChWifi, sonoff4ChFirebase, sonoff4ChHybrid);
        put(BoardId.Sonoff4CH, sonoff4ChSet);
        Firmware sonoffPowWifi = new Firmware(Variant.WiFi, "sonoffpow/firmware_wifi.bin", "46efc21a6a97d8987cdd1807b9836c40", 1, 4);
        Firmware sonoffPowFirebase = new Firmware(Variant.Firebase, "sonoffpow/firmware_firebase.bin", "669404bddde69c95dffb94666ebeb557", 1, 4);
        Firmware sonoffPowHybrid = new Firmware(Variant.Hybrid, "sonoffpow/firmware_hybrid.bin", "35431838f318d7e72823c2ca1ee6da94", 1, 4);
        FirmwareSet sonoffPowSet = new FirmwareSet(sonoffPowWifi, sonoffPowFirebase, sonoffPowHybrid);
        put(BoardId.SonoffPOW, sonoffPowSet);
        Firmware h802Wifi = new Firmware(Variant.WiFi, "h802/firmware_wifi.bin", "3fc7163436b27b7d37e84fed3a48731e", 1, 4);
        Firmware h802Firebase = new Firmware(Variant.Firebase, "h802/firmware_firebase.bin", "c6f75df33139b6bef5a561ab1f86cb1c", 1, 4);
        Firmware h802Hybrid = new Firmware(Variant.Hybrid, "h802/firmware_hybrid.bin", "d5dbc777969574a34e10ab262ee4c352", 1, 4);
        FirmwareSet h802Set = new FirmwareSet(h802Wifi, h802Firebase, h802Hybrid);
        put(BoardId.H802, h802Set);
        Firmware sonoffBasicWifi = new Firmware(Variant.WiFi, "sonoffbasic/firmware_wifi.bin", "81a14fa0dfc6807ee21b607fbfcce826", 1, 4);
        Firmware sonoffBasicFirebase = new Firmware(Variant.Firebase, "sonoffbasic/firmware_firebase.bin", "75a8e423806241efca388a0ede10c48d", 1, 4);
        Firmware sonoffBasicHybrid = new Firmware(Variant.Hybrid, "sonoffbasic/firmware_hybrid.bin", "6d403bbedd3ea5cb36fee1e63a835565", 1, 4);
        FirmwareSet sonoffBasicSet = new FirmwareSet(sonoffBasicWifi, sonoffBasicFirebase, sonoffBasicHybrid);
        put(BoardId.SonoffBASIC, sonoffBasicSet);
        Firmware electrodragon2RelWifi = new Firmware(Variant.WiFi, "electrodragon2rel/firmware_wifi.bin", "0a53a669e5caa3110132281189151052", 1, 4);
        Firmware electrodragon2RelFirebase = new Firmware(Variant.Firebase, "electrodragon2rel/firmware_firebase.bin", "f265ae4ff6653fd7489984ce38a76339", 1, 4);
        Firmware electrodragon2RelHybrid = new Firmware(Variant.Hybrid, "electrodragon2rel/firmware_hybrid.bin", "8ee8b374ccfb7a79ff48da0077df966f", 1, 4);
        FirmwareSet electrodragon2RelSet = new FirmwareSet(electrodragon2RelWifi, electrodragon2RelFirebase, electrodragon2RelHybrid);
        put(BoardId.Elecrodragon2REL, electrodragon2RelSet);
        Firmware electrodragon2RelSpdtWifi = new Firmware(Variant.WiFi, "electrodragon2rel_spdt/firmware_wifi.bin", "c48d7d7a54cda3333ea7234f6a24eaf1", 1, 4);
        Firmware electrodragon2RelSpdtFirebase = new Firmware(Variant.Firebase, "electrodragon2rel_spdt/firmware_firebase.bin", "a9b2853c2cdf83e188b53b8f0977c349", 1, 4);
        Firmware electrodragon2RelSpdtHybrid = new Firmware(Variant.Hybrid, "electrodragon2rel_spdt/firmware_hybrid.bin", "0be3d83660cc390fb0769607f00c82dc", 1, 4);
        FirmwareSet electrodragon2RelSpdtSet = new FirmwareSet(electrodragon2RelSpdtWifi, electrodragon2RelSpdtFirebase, electrodragon2RelSpdtHybrid);
        put(BoardId.Elecrodragon2REL_SPDT, electrodragon2RelSpdtSet);
        Firmware electrodragonLedWifi = new Firmware(Variant.WiFi, "electrodragon_led/firmware_wifi.bin", "9a980143f1dc2a784a111c6ffbefaae8", 1, 4);
        Firmware electrodragonLedFirebase = new Firmware(Variant.Firebase, "electrodragon_led/firmware_firebase.bin", "296336257f925468365b675380615fc5", 1, 4);
        Firmware electrodragonLedHybrid = new Firmware(Variant.Hybrid, "electrodragon_led/firmware_hybrid.bin", "aa7c28d9728cfccb55b513d1feb36bbe", 1, 4);
        FirmwareSet electrodragonLedSet = new FirmwareSet(electrodragonLedWifi, electrodragonLedFirebase, electrodragonLedHybrid);
        put(BoardId.ElectrodragonLed, electrodragonLedSet);
    }
}