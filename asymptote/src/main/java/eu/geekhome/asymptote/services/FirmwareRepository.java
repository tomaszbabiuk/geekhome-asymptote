package eu.geekhome.asymptote.services;

import eu.geekhome.asymptote.model.BoardId;
import eu.geekhome.asymptote.model.Firmware;
import eu.geekhome.asymptote.model.FirmwareSet;
import eu.geekhome.asymptote.model.Variant;
import eu.geekhome.asymptote.model.SystemInformation;

public interface FirmwareRepository {
    boolean isLatest(SystemInformation systemInformation);
    Firmware findFirmware(BoardId boardId, Variant variant);
    FirmwareSet findFirmwareSet(BoardId boardId);
}
