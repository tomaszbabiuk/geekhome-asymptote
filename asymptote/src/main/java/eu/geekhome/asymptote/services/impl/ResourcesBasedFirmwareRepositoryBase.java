package eu.geekhome.asymptote.services.impl;

import java.util.Hashtable;

import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.FirmwareSet;
import eu.geekhome.asymptote.model.SystemInformation;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.services.FirmwareRepository;

class ResourcesBasedFirmwareRepositoryBase implements FirmwareRepository {
    private Hashtable<BoardId, FirmwareSet> _firmwareSets = new Hashtable<>();

    @Override
    public boolean isLatest(SystemInformation systemInformation) {
        Firmware firmware = findFirmware(systemInformation.getBoardId(), systemInformation.getVariant());
        if (firmware != null) {
            int sensorVersion = systemInformation.getVersionMajor() * 256 +
                    systemInformation.getVersionMinor();
            int availableFirmwareVersion = firmware.getVersionMajor() * 256 +
                    firmware.getVersionMinor();
            return availableFirmwareVersion <= sensorVersion;
        }

        return true;
    }

    @Override
    public Firmware findFirmware(BoardId boardId, Variant variant) {
        FirmwareSet set = findFirmwareSet(boardId);
        if (set != null) {
            return variant == Variant.WiFi ? set.getWifiFirmware() : set.getFirebaseFirmware();
        }

        return null;
    }

    @Override
    public FirmwareSet findFirmwareSet(BoardId boardId) {
        if (_firmwareSets.containsKey(boardId)) {
            return _firmwareSets.get(boardId);
        }

        return null;
    }

    protected void put(BoardId boardId, FirmwareSet set) {
        _firmwareSets.put(boardId, set);
    }
}
