package eu.geekhome.asymptote.services;

public interface PasswordService {
    void setRememberEmergencyPassword(boolean remember);

    void setRememberCloudCredentials(boolean remember);

    void setEmergencyPassword(String emergencyPassword);

    void setCloudPassword(String cloudPassword);

    void setCloudEmail(String cloudUsername);

    boolean isRememberEmergencyPassword();

    boolean isRememberCloudCredentials();

    String getEmergencyPassword();

    String getCloudPassword();

    String getCloudEmail();
}
