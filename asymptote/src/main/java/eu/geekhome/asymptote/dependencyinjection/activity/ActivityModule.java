package eu.geekhome.asymptote.dependencyinjection.activity;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.CloudCertificateChecker;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FavoriteColorsService;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.LocalDiscoveryService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.OtaServer;
import eu.geekhome.asymptote.services.PrivacyService;
import eu.geekhome.asymptote.services.SyncManager;
import eu.geekhome.asymptote.services.ThreadRunner;
import eu.geekhome.asymptote.services.ToastService;
import eu.geekhome.asymptote.services.UdpService;
import eu.geekhome.asymptote.services.impl.AndroidGeneralDialogService;
import eu.geekhome.asymptote.services.impl.AndroidThreadRunner;
import eu.geekhome.asymptote.services.impl.AndroidToastService;
import eu.geekhome.asymptote.services.impl.AndroidUdpService;
import eu.geekhome.asymptote.services.impl.BrokenOtaServer;
import eu.geekhome.asymptote.services.impl.ColorOMaticColorDialogService;
import eu.geekhome.asymptote.services.impl.FirebaseCertificateChecker;
import eu.geekhome.asymptote.services.impl.FirebaseCloudDeviceService;
import eu.geekhome.asymptote.services.impl.FirebaseCloudUserService;
import eu.geekhome.asymptote.services.impl.FragmentBasedNavigationService;
import eu.geekhome.asymptote.services.impl.HttpClientSyncManager;
import eu.geekhome.asymptote.services.impl.NanoOtaServer;
import eu.geekhome.asymptote.services.impl.PreferencesAddressesPersistenceService;
import eu.geekhome.asymptote.services.impl.PreferencesFavoriteColorsService;
import eu.geekhome.asymptote.services.impl.PreferencesPrivacyService;
import eu.geekhome.asymptote.services.impl.UdpLocalDiscoveryService;
import eu.geekhome.asymptote.viewmodel.ControlsCreator;

@Module
public class ActivityModule {
    private FragmentActivity _activity;

    public ActivityModule(FragmentActivity activity) {
        _activity = activity;
    }

    @Provides
    @ActivityScope
    Activity provideActivity() {
        return _activity;
    }

    @Provides
    @ActivityScope
    Context provideContext() {
        return _activity;
    }

    @Provides
    @ActivityScope
    ToastService provideToastService() {
        return new AndroidToastService(_activity);
    }

    @Provides
    @ActivityScope
    PrivacyService providePrivacyService() {
        return new PreferencesPrivacyService(_activity);
    }

    @Provides
    @ActivityScope
    AddressesPersistenceService provideAddressesPersistenceService() {
        return new PreferencesAddressesPersistenceService(_activity);
    }

    @Provides
    @ActivityScope
    FavoriteColorsService provideFavoriteColorsService() {
        return new PreferencesFavoriteColorsService(_activity);
    }

    @Provides
    @ActivityScope
    NavigationService provideNavigationService() {
        return new FragmentBasedNavigationService(_activity.getSupportFragmentManager(), _activity);
    }

    @Provides
    @ActivityScope
    ThreadRunner provideThreadRunner() {
        return new AndroidThreadRunner(_activity);
    }

    @Provides
    @ActivityScope
    UdpService provideUdpService(ThreadRunner threadRunner) {
        return new AndroidUdpService(_activity, threadRunner);
    }

    @Provides
    @ActivityScope
    ColorDialogService provideColorDialogService() {
        return new ColorOMaticColorDialogService(_activity);
    }

    @Provides
    @ActivityScope
    LocalDiscoveryService provideLocalDiscoveryService(UdpService udpService) {
        return new UdpLocalDiscoveryService(udpService);
    }

    @Provides
    @ActivityScope
    GeneralDialogService provideGeneralDialogService() {
        return new AndroidGeneralDialogService(_activity, _activity.getFragmentManager());
    }

    @Provides
    @ActivityScope
    OtaServer provideOtaServer() {
        try {
            return new NanoOtaServer(_activity.getApplicationContext());
        } catch (Exception e) {
            return new BrokenOtaServer();
        }
    }

    @Provides
    @ActivityScope
    SyncManager provideSyncManager(Activity activity, LocalDiscoveryService localDiscoveryService,
                                   AddressesPersistenceService addressesPersistenceService,
                                   ThreadRunner threadRunner, EmergencyManager emergencyManager) {
        try {
            return new HttpClientSyncManager(activity, localDiscoveryService, addressesPersistenceService,
                    threadRunner, emergencyManager);
        } catch (Exception e) {
            return null;
        }
    }

    @Provides
    @ActivityScope
    CloudUserService provideCloudUserService() {
        return new FirebaseCloudUserService(_activity);
    }

    @Provides
    @ActivityScope
    CloudDeviceService provideCloudDeviceService() {
        return new FirebaseCloudDeviceService();
    }

    @Provides
    @ActivityScope
    CloudCertificateChecker provideCloudChecker() {
        return new FirebaseCertificateChecker("https://asymptote-769eb.firebaseio.com");
    }
}
