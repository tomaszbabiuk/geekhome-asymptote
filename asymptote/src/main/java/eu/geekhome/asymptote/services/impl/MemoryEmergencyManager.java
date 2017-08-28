package eu.geekhome.asymptote.services.impl;

import eu.geekhome.asymptote.services.EmergencyManager;

public class MemoryEmergencyManager implements EmergencyManager {
    private boolean _emergency;
    private String _password;

    public MemoryEmergencyManager(boolean emergency, String password) {
        _emergency = emergency;
        _password = password;
    }

    @Override
    public boolean isEmergency() {
        return _emergency;
    }

    @Override
    public void setPassword(String password) {
        _password = password;
    }

    @Override
    public String getPassword() {
        return _password;
    }

    @Override
    public void setEmergency(boolean emergency) {
        _emergency = emergency;
    }
}
