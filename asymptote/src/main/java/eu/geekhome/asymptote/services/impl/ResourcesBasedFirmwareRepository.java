package eu.geekhome.asymptote.services.impl;
import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.FirmwareSet;
import eu.geekhome.asymptote.model.Variant;
public class ResourcesBasedFirmwareRepository extends ResourcesBasedFirmwareRepositoryBase {
    public ResourcesBasedFirmwareRepository() {
        Firmware sonoffTHWifi = new Firmware(Variant.WiFi, "sonoffth/firmware_wifi.bin", "da5088e0c3723d6bc6f6260f5ac93d75", 1, 5);
        Firmware sonoffTHFirebase = new Firmware(Variant.Firebase, "sonoffth/firmware_firebase.bin", "35694eba97783666e1ac561c4ec784c4", 1, 5);
        Firmware sonoffTHHybrid = new Firmware(Variant.Hybrid, "sonoffth/firmware_hybrid.bin", "22e4114a96cec043858950604eab6cb5", 1, 5);
        FirmwareSet sonoffTHSet = new FirmwareSet(sonoffTHWifi, sonoffTHFirebase, sonoffTHHybrid);
        put(BoardId.SonoffTH_None, sonoffTHSet);
        put(BoardId.SonoffTH_AM2301, sonoffTHSet);
        put(BoardId.SonoffTH_DS18B20, sonoffTHSet);
        Firmware sonoffSCWifi = new Firmware(Variant.WiFi, "sonoffsc/firmware_wifi.bin", "f2ed2461d53e81839afd86c734441006", 1, 4);
        Firmware sonoffSCFirebase = new Firmware(Variant.Firebase, "sonoffsc/firmware_firebase.bin", "37213c0674385902a77037eb9af7279f", 1, 4);
        Firmware sonoffSCHybrid = new Firmware(Variant.Hybrid, "sonoffsc/firmware_hybrid.bin", "42716d29a979b5245a76a738a5dcfef4", 1, 4);
        FirmwareSet sonoffSCSet = new FirmwareSet(sonoffSCWifi, sonoffSCFirebase, sonoffSCHybrid);
        put(BoardId.SonoffSC, sonoffSCSet);
        Firmware sonoffTouchWifi = new Firmware(Variant.WiFi, "sonofftouch/firmware_wifi.bin", "d35a5881761aa79a758854631456ef1a", 1, 4);
        Firmware sonoffTouchFirebase = new Firmware(Variant.Firebase, "sonofftouch/firmware_firebase.bin", "2091aad2e1db8423835ad4a232384165", 1, 4);
        Firmware sonoffTouchHybrid = new Firmware(Variant.Hybrid, "sonofftouch/firmware_hybrid.bin", "c3d1a56ab8c1a7c9f84e27906afac2aa", 1, 4);
        FirmwareSet sonoffTouchSet = new FirmwareSet(sonoffTouchWifi, sonoffTouchFirebase, sonoffTouchHybrid);
        put(BoardId.SonoffTOUCH, sonoffTouchSet);
        Firmware sonoffDualWifi = new Firmware(Variant.WiFi, "sonoffdual/firmware_wifi.bin", "ff6851762c46da7d0c9c50932970f217", 1, 4);
        Firmware sonoffDualFirebase = new Firmware(Variant.Firebase, "sonoffdual/firmware_firebase.bin", "cd80bf39101313515f5c222ed20ff4e6", 1, 4);
        Firmware sonoffDualHybrid = new Firmware(Variant.Hybrid, "sonoffdual/firmware_hybrid.bin", "957a32d4aa333cd4a69795ee33eaf222", 1, 4);
        FirmwareSet sonoffDualSet = new FirmwareSet(sonoffDualWifi, sonoffDualFirebase, sonoffDualHybrid);
        put(BoardId.SonoffDUAL, sonoffDualSet);
        Firmware sonoff4ChWifi = new Firmware(Variant.WiFi, "sonoff4ch/firmware_wifi.bin", "090a3f1d6d613c0e47021161e4301606", 1, 4);
        Firmware sonoff4ChFirebase = new Firmware(Variant.Firebase, "sonoff4ch/firmware_firebase.bin", "fccc05eaf115d33df588423e4f8471c0", 1, 4);
        Firmware sonoff4ChHybrid = new Firmware(Variant.Hybrid, "sonoff4ch/firmware_hybrid.bin", "beea2d0aa7d957a10af14ad386b06529", 1, 4);
        FirmwareSet sonoff4ChSet = new FirmwareSet(sonoff4ChWifi, sonoff4ChFirebase, sonoff4ChHybrid);
        put(BoardId.Sonoff4CH, sonoff4ChSet);
        Firmware sonoffPowWifi = new Firmware(Variant.WiFi, "sonoffpow/firmware_wifi.bin", "e25f84cfbc03bd6bba7b111ca24c232f", 1, 4);
        Firmware sonoffPowFirebase = new Firmware(Variant.Firebase, "sonoffpow/firmware_firebase.bin", "23a6127cd420dfd526f0b4cbb411ecfb", 1, 4);
        Firmware sonoffPowHybrid = new Firmware(Variant.Hybrid, "sonoffpow/firmware_hybrid.bin", "253e9091ca2ba2ce632a33151f01a5a3", 1, 4);
        FirmwareSet sonoffPowSet = new FirmwareSet(sonoffPowWifi, sonoffPowFirebase, sonoffPowHybrid);
        put(BoardId.SonoffPOW, sonoffPowSet);
        Firmware h802Wifi = new Firmware(Variant.WiFi, "h802/firmware_wifi.bin", "aec782fb7760e649b4ac8e8c5e5eafac", 1, 4);
        Firmware h802Firebase = new Firmware(Variant.Firebase, "h802/firmware_firebase.bin", "48498ed039dfd93d0df7b0fde7fc714d", 1, 4);
        Firmware h802Hybrid = new Firmware(Variant.Hybrid, "h802/firmware_hybrid.bin", "aafb6476d426e0118c40609fed7f64f1", 1, 4);
        FirmwareSet h802Set = new FirmwareSet(h802Wifi, h802Firebase, h802Hybrid);
        put(BoardId.H802, h802Set);
        Firmware sonoffBasicWifi = new Firmware(Variant.WiFi, "sonoffbasic/firmware_wifi.bin", "2d43cb4a5209b88cd698f2d0b5ed28e0", 1, 4);
        Firmware sonoffBasicFirebase = new Firmware(Variant.Firebase, "sonoffbasic/firmware_firebase.bin", "73249c6f615f9f0f43472fa9101064be", 1, 4);
        Firmware sonoffBasicHybrid = new Firmware(Variant.Hybrid, "sonoffbasic/firmware_hybrid.bin", "7ed046008549681d54fbd778582c6f50", 1, 4);
        FirmwareSet sonoffBasicSet = new FirmwareSet(sonoffBasicWifi, sonoffBasicFirebase, sonoffBasicHybrid);
        put(BoardId.SonoffBASIC, sonoffBasicSet);
        Firmware electrodragon2RelWifi = new Firmware(Variant.WiFi, "electrodragon2rel/firmware_wifi.bin", "dec9b816ea974940e4681bd874bc1341", 1, 4);
        Firmware electrodragon2RelFirebase = new Firmware(Variant.Firebase, "electrodragon2rel/firmware_firebase.bin", "6af7e1dda2828c4c4fb95ebf36023a03", 1, 4);
        Firmware electrodragon2RelHybrid = new Firmware(Variant.Hybrid, "electrodragon2rel/firmware_hybrid.bin", "b3024c764403d0e945718ca7b567adc8", 1, 4);
        FirmwareSet electrodragon2RelSet = new FirmwareSet(electrodragon2RelWifi, electrodragon2RelFirebase, electrodragon2RelHybrid);
        put(BoardId.Electrodragon2REL_None, electrodragon2RelSet);
        put(BoardId.Electrodragon2REL_DS18B20, electrodragon2RelSet);
        put(BoardId.Electrodragon2REL_AM2301, electrodragon2RelSet);


        Firmware electrodragon2RelSpdtWifi = new Firmware(Variant.WiFi, "electrodragon2rel_spdt/firmware_wifi.bin", "c48d7d7a54cda3333ea7234f6a24eaf1", 1, 4);
        Firmware electrodragon2RelSpdtFirebase = new Firmware(Variant.Firebase, "electrodragon2rel_spdt/firmware_firebase.bin", "a9b2853c2cdf83e188b53b8f0977c349", 1, 4);
        Firmware electrodragon2RelSpdtHybrid = new Firmware(Variant.Hybrid, "electrodragon2rel_spdt/firmware_hybrid.bin", "0be3d83660cc390fb0769607f00c82dc", 1, 4);
        FirmwareSet electrodragon2RelSpdtSet = new FirmwareSet(electrodragon2RelSpdtWifi, electrodragon2RelSpdtFirebase, electrodragon2RelSpdtHybrid);
        put(BoardId.Elecrodragon2REL_SPDT, electrodragon2RelSpdtSet);

        Firmware electrodragonLedWifi = new Firmware(Variant.WiFi, "electrodragon_led/firmware_wifi.bin", "f00002872b815f697ea40cc016edc380", 1, 4);
        Firmware electrodragonLedFirebase = new Firmware(Variant.Firebase, "electrodragon_led/firmware_firebase.bin", "e95a9e2e1a406826eb185d62b69596df", 1, 4);
        Firmware electrodragonLedHybrid = new Firmware(Variant.Hybrid, "electrodragon_led/firmware_hybrid.bin", "1e74fa237d7b905e6672514b447bcd9f", 1, 4);
        FirmwareSet electrodragonLedSet = new FirmwareSet(electrodragonLedWifi, electrodragonLedFirebase, electrodragonLedHybrid);
        put(BoardId.ElectrodragonLed, electrodragonLedSet);

        Firmware geekGateWifi = new Firmware(Variant.WiFi, "geekgate/firmware_wifi.bin", "77e8eee7679e5624ebc930c72f819300", 1, 4);
        Firmware geekGateFirebase = new Firmware(Variant.Firebase, "geekgate/firmware_firebase.bin", "6559dee7a23a9bd553a26414f29623f2", 1, 4);
        Firmware geekGateHybrid = new Firmware(Variant.Hybrid, "geekgate/firmware_hybrid.bin", "b9a3fec7478934998bed2dcc87f3cb72", 1, 4);
        FirmwareSet geekGateSet = new FirmwareSet(geekGateWifi, geekGateFirebase, geekGateHybrid);
        put(BoardId.geekGATE, geekGateSet);
    }
}