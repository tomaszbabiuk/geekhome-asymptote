package eu.geekhome.asymptote.services;

public interface EmergencyManager {
    boolean isEmergency();
    void setPassword(String password);
    String getPassword();
    void setEmergency(boolean emergency);
}
