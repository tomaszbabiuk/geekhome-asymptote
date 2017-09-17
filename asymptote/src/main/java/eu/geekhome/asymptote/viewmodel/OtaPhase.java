package eu.geekhome.asymptote.viewmodel;

import android.support.annotation.StringRes;

import eu.geekhome.asymptote.R;

public enum OtaPhase {
    ServerStarting(R.string.ota_starting_update_server),
    WaitingForDevice(R.string.ota_waiting_for_device),
    ServerError(R.string.ota_server_error),
    InProgress(R.string.ota_int_progress),
    Finished(R.string.finished);

    private int _instructionResourceId;

    OtaPhase(@StringRes int instructionResourceId) {

        _instructionResourceId = instructionResourceId;
    }

    public int getInstructionResourceId() {
        return _instructionResourceId;
    }
}