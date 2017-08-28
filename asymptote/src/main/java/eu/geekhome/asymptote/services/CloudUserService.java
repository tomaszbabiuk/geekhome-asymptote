package eu.geekhome.asymptote.services;

import eu.geekhome.asymptote.model.CloudUser;

public interface CloudUserService {
    void signInWithEmailAndPassword(String email, String password, UserCallback callback);
    void signUpWithEmailAndPassword(String email, String password, UserCallback callback);
    void resendVerificationEmail(String email, String password, UserCallback callback);
    void cancelRegistration(String email, String password, UserCallback callback);
    void linkAnonymousWithEmailProvider(String email, String password, UserCallback callback);
    void changeEmail(String password, String newEmail, CloudActionCallback<Void> callback);
    void changePassword(String oldPassword, String newPassword, CloudActionCallback<Void> callback);
    void rememberEmergencyPassword(String userId, String emergencyPassword, CloudActionCallback<Void> callback);
    void refreshUser(UserCallback callback);
    void resetPassword(String email, CloudActionCallback<Void> callback);
    void signOut();

    CloudUser getCachedUser();

    interface UserCallback extends CloudActionCallback<CloudUser> {
        void success(CloudUser user);
        void failure(CloudException exception);
    }
}
