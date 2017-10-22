package eu.geekhome.asymptote.dependencyinjection.activity;

import android.support.v7.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FavoriteColorsService;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.LocalDiscoveryService;
import eu.geekhome.asymptote.services.LogoutService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.OtaServer;
import eu.geekhome.asymptote.services.PasswordService;
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
import eu.geekhome.asymptote.services.impl.FirebaseCloudDeviceService;
import eu.geekhome.asymptote.services.impl.FirebaseCloudUserService;
import eu.geekhome.asymptote.services.impl.FragmentBasedNavigationService;
import eu.geekhome.asymptote.services.impl.HttpClientSyncManager;
import eu.geekhome.asymptote.services.impl.HybridLogoutService;
import eu.geekhome.asymptote.services.impl.NanoOtaServer;
import eu.geekhome.asymptote.services.impl.PreferencesAddressesPersistenceService;
import eu.geekhome.asymptote.services.impl.PreferencesFavoriteColorsService;
import eu.geekhome.asymptote.services.impl.UdpLocalDiscoveryService;
import eu.geekhome.asymptote.utils.AutomationBuilder;

@Module
public abstract class GeneralActivityModule<T extends AppCompatActivity> {
    @Provides
    @ActivityScope
    ToastService provideToastService(T activity) {
        return new AndroidToastService(activity);
    }

    @Provides
    @ActivityScope
    AddressesPersistenceService provideAddressesPersistenceService(T activity) {
        return new PreferencesAddressesPersistenceService(activity);
    }

    @Provides
    @ActivityScope
    FavoriteColorsService provideFavoriteColorsService(T activity) {
        return new PreferencesFavoriteColorsService(activity);
    }

    @Provides
    @ActivityScope
    NavigationService provideNavigationService(T activity) {
        return new FragmentBasedNavigationService(activity.getSupportFragmentManager(), activity);
    }

    @Provides
    @ActivityScope
    ThreadRunner provideThreadRunner(T activity) {
        return new AndroidThreadRunner(activity);
    }

    @Provides
    @ActivityScope
    UdpService provideUdpService(T activity, ThreadRunner threadRunner) {
        return new AndroidUdpService(activity, threadRunner);
    }

    @Provides
    @ActivityScope
    ColorDialogService provideColorDialogService(T activity) {
        return new ColorOMaticColorDialogService(activity);
    }

    @Provides
    @ActivityScope
    LocalDiscoveryService provideLocalDiscoveryService(UdpService udpService) {
        return new UdpLocalDiscoveryService(udpService);
    }

    @Provides
    @ActivityScope
    GeneralDialogService provideGeneralDialogService(T activity) {
        return new AndroidGeneralDialogService(activity, activity.getFragmentManager());
    }

    @Provides
    @ActivityScope
    OtaServer provideOtaServer(T activity) {
        try {
            return new NanoOtaServer(activity.getApplicationContext());
        } catch (Exception e) {
            return new BrokenOtaServer();
        }
    }

    @Provides
    @ActivityScope
    SyncManager provideSyncManager(T activity, LocalDiscoveryService localDiscoveryService,
                                   AddressesPersistenceService addressesPersistenceService,
                                   ThreadRunner threadRunner, EmergencyManager emergencyManager,
                                   AutomationBuilder automationBuilder) {
        try {
            return new HttpClientSyncManager(activity, localDiscoveryService, addressesPersistenceService,
                    threadRunner, emergencyManager, automationBuilder);
        } catch (Exception e) {
            return null;
        }
    }

    @Provides
    @ActivityScope
    CloudUserService provideCloudUserService(T activity) {
        return new FirebaseCloudUserService(activity);
    }

    @Provides
    @ActivityScope
    CloudDeviceService provideCloudDeviceService(T activity, AutomationBuilder automationBuilder) {
        return new FirebaseCloudDeviceService(activity, automationBuilder);
    }

    @Provides
    @ActivityScope
    LogoutService provideLogoutService(EmergencyManager emergencyManager, PasswordService passwordService,
                                       NavigationService navigationService, CloudUserService cloudUserService) {
        return new HybridLogoutService(emergencyManager, passwordService, navigationService, cloudUserService);
    }
}
