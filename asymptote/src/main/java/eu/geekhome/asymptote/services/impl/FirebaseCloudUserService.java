package eu.geekhome.asymptote.services.impl;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.model.CloudUser;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.services.CloudActionCallback;
import eu.geekhome.asymptote.services.CloudException;
import eu.geekhome.asymptote.services.CloudUserService;

public class FirebaseCloudUserService implements CloudUserService {
    private final Activity _activity;
    private final TaskResultConverter<AuthResult, CloudUser> _firebaseUserToCloudUserConverter;
    private final TaskResultConverter<Void, Void> _voidConverter;
    private final TaskResultConverter<Void, CloudUser> _voidToUserConverter;
    private FirebaseAuth _auth;

    public FirebaseCloudUserService(Activity activity) {
        _activity = activity;
        _auth = FirebaseAuth.getInstance();

        _firebaseUserToCloudUserConverter = new TaskResultConverter<AuthResult, CloudUser>() {
            @Override
            public CloudUser convert(AuthResult authResult) {
                return toCloudUser(authResult.getUser());
            }
        };

        _voidConverter = new TaskResultConverter<Void, Void>() {
            @Override
            public Void convert(Void aVoid) {
                return null;
            }
        };

        _voidToUserConverter = new TaskResultConverter<Void, CloudUser>() {
            @Override
            public CloudUser convert(Void aVoid) {
                FirebaseUser fireBaseUser = _auth.getCurrentUser();
                return toCloudUser(fireBaseUser);
            }
        };
    }

    @Override
    public void signInWithEmailAndPassword(String email, String password, final UserCallback callback) {
        _auth.signOut();
        _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
            }
        });
    }

    @Override
    public void signUpWithEmailAndPassword(String email, String password, final UserCallback callback) {
        _auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> verifyTask) {
                            if (verifyTask.isSuccessful()) {
                                callback.success(toCloudUser(firebaseUser));
                            } else {
                                handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                            }
                        }
                    });
                } else {
                    handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                }
            }
        });
    }

    @Override
    public void linkAnonymousWithEmailProvider(String email, String password, final UserCallback callback) {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        if (_auth.getCurrentUser() != null) {
            _auth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        final FirebaseUser firebaseUser = task.getResult().getUser();
                        firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> verifyTask) {
                                if (verifyTask.isSuccessful()) {
                                    callback.success(toCloudUser(firebaseUser));
                                } else {
                                    handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                                }
                            }
                        });
                    } else {
                        handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                    }
                }
            });
        } else {
            callbackUserNotFoundFailure(callback);
        }
    }

    @Override
    public void resendVerificationEmail(String email, String password, final UserCallback callback) {
        _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> verifyTask) {
                            if (verifyTask.isSuccessful()) {
                                callback.success(toCloudUser(firebaseUser));
                            } else {
                                handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                            }
                        }
                    });
                } else {
                    handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                }
            }
        });
    }

    @Override
    public void cancelRegistration(String email, String password, final UserCallback callback) {
        _auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final FirebaseUser firebaseUser = task.getResult().getUser();
                    firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> verifyTask) {
                            if (verifyTask.isSuccessful()) {
                                callback.success(toCloudUser(firebaseUser));
                            } else {
                                handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                            }
                        }
                    });
                } else {
                    handleTaskResult(task, callback, _firebaseUserToCloudUserConverter);
                }
            }
        });
    }

    @Override
    public void refreshUser(final UserCallback callback) {
        if (_auth.getCurrentUser() != null) {
            _auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (callback != null) {
                        handleTaskResult(task, callback, _voidToUserConverter);
                    }
                }
            });
        } else {
            callback.success(null);
        }
    }

    @Override
    public void changePassword(String oldPassword, final String newPassword, final CloudActionCallback<Void> callback) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {
            _auth.signInWithEmailAndPassword(user.getEmail(), oldPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user.updatePassword(newPassword).addOnCompleteListener(_activity, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                handleTaskResult(task, callback, _voidConverter);
                            }
                        });
                    } else {
                        callback.failure(new CloudException(task.getException(), false));
                    }
                }
            });
        } else {
            callbackUserNotFoundFailure(callback);
        }
    }

    @Override
    public void rememberEmergencyPassword(String userId, String emergencyPassword, final CloudActionCallback<Void> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("users")
                .child(userId)
                .child("emergencyPassword")
                .setValue(emergencyPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.success(null);
                        } else {
                            CloudException cex = new CloudException(task.getException(), false);
                            callback.failure(cex);
                        }
                    }
                });
    }

    @Override
    public void changeEmail(String password, final String newEmail, final CloudActionCallback<Void> callback) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getEmail() != null) {
            final OnCompleteListener<Void> userReloadedListener = new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    refreshUser(new UserCallback() {
                        @Override
                        public void success(CloudUser user) {
                            callback.success(null);
                        }

                        @Override
                        public void failure(CloudException exception) {
                            callback.failure(toCloudException(exception));
                        }
                    });
                }
            };

            OnCompleteListener<AuthResult> signInCompletedListener = new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user.updateEmail(newEmail).addOnCompleteListener(_activity, userReloadedListener);
                    } else {
                        callback.failure(toCloudException(task.getException()));
                    }
                }
            };

            _auth.signInWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(signInCompletedListener);
        } else {
            callbackUserNotFoundFailure(callback);
        }
    }

    @Override
    public void resetPassword(String email, final CloudActionCallback<Void> callback) {
        _auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                handleTaskResult(task, callback, _voidConverter);
            }
        });
    }

    @Override
    public void signOut() {
        _auth.signOut();
    }

    @Override
    public CloudUser getCachedUser() {
        FirebaseUser user = _auth.getCurrentUser();
        if (user == null) {
            return null;
        }

        return new CloudUser(user.getUid(), user.getEmail(), user.isEmailVerified());
    }

    private interface TaskResultConverter<TInput, TOutput> {
        TOutput convert(TInput input);
    }

    private void callbackUserNotFoundFailure(CloudActionCallback callback) {
        CloudException userNotFoundException = new CloudException(_activity.getString(R.string.user_not_found), false);
        callback.failure(userNotFoundException);
    }

    private CloudUser toCloudUser(FirebaseUser firebaseUser) {
        return new CloudUser(firebaseUser.getUid(), firebaseUser.getEmail(), firebaseUser.isEmailVerified());
    }

    private CloudException toCloudException(Exception ex) {
        //single place to handle all firebase exceptions :)
        if (ex instanceof FirebaseAuthUserCollisionException) {
            String exceptionMessage = _activity.getString(R.string.choose_different_username);
            return new CloudException(exceptionMessage, false);
        } else if (ex instanceof FirebaseAuthException) {
            String exceptionMessage = _activity.getString(R.string.invalid_username_or_password);
            return new CloudException(exceptionMessage, true);
        } else if (ex instanceof FirebaseApiNotAvailableException) {
            String exceptionMessage = _activity.getString(R.string.old_google_play_services);
            return new CloudException(exceptionMessage, false);
        } else {
            return new CloudException(ex, false);
        }
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private <TTask,TResult> void handleTaskResult(Task<TTask> task, CloudActionCallback<TResult> callback, TaskResultConverter<TTask, TResult> converter) {
        if (task.isSuccessful()) {
            TResult result = converter.convert(task.getResult());
            callback.success(result);
        } else {
            CloudException cloudEx = toCloudException(task.getException());
            callback.failure(cloudEx);
        }
    }
}
