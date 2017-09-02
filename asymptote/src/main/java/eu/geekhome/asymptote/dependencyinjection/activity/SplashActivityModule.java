package eu.geekhome.asymptote.dependencyinjection.activity;

import dagger.Module;
import dagger.Provides;
import eu.geekhome.asymptote.SplashActivity;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FavoriteColorsService;
import eu.geekhome.asymptote.services.GeneralDialogService;
import eu.geekhome.asymptote.services.LocalDiscoveryService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.OtaServer;
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
import eu.geekhome.asymptote.services.impl.NanoOtaServer;
import eu.geekhome.asymptote.services.impl.PreferencesAddressesPersistenceService;
import eu.geekhome.asymptote.services.impl.PreferencesFavoriteColorsService;
import eu.geekhome.asymptote.services.impl.SplashViewModelsFactory;
import eu.geekhome.asymptote.services.impl.UdpLocalDiscoveryService;

@Module
public class SplashActivityModule {
    @Provides
    @ActivityScope
    ToastService provideToastService(SplashActivity splashActivity) {
        return new AndroidToastService(splashActivity);
    }

    @Provides
    @ActivityScope
    AddressesPersistenceService provideAddressesPersistenceService(SplashActivity splashActivity) {
        return new PreferencesAddressesPersistenceService(splashActivity);
    }

    @Provides
    @ActivityScope
    FavoriteColorsService provideFavoriteColorsService(SplashActivity splashActivity) {
        return new PreferencesFavoriteColorsService(splashActivity);
    }

    @Provides
    @ActivityScope
    NavigationService provideNavigationService(SplashActivity splashActivity) {
        return new FragmentBasedNavigationService(splashActivity.getSupportFragmentManager(), splashActivity);
    }

    @Provides
    @ActivityScope
    ThreadRunner provideThreadRunner(SplashActivity splashActivity) {
        return new AndroidThreadRunner(splashActivity);
    }

    @Provides
    @ActivityScope
    UdpService provideUdpService(SplashActivity splashActivity, ThreadRunner threadRunner) {
        return new AndroidUdpService(splashActivity, threadRunner);
    }

    @Provides
    @ActivityScope
    ColorDialogService provideColorDialogService(SplashActivity splashActivity) {
        return new ColorOMaticColorDialogService(splashActivity);
    }

    @Provides
    @ActivityScope
    LocalDiscoveryService provideLocalDiscoveryService(UdpService udpService) {
        return new UdpLocalDiscoveryService(udpService);
    }

    @Provides
    @ActivityScope
    GeneralDialogService provideGeneralDialogService(SplashActivity splashActivity) {
        return new AndroidGeneralDialogService(splashActivity, splashActivity.getFragmentManager());
    }

    @Provides
    @ActivityScope
    OtaServer provideOtaServer(SplashActivity splashActivity) {
        try {
            return new NanoOtaServer(splashActivity.getApplicationContext());
        } catch (Exception e) {
            return new BrokenOtaServer();
        }
    }

    @Provides
    @ActivityScope
    SyncManager provideSyncManager(SplashActivity activity, LocalDiscoveryService localDiscoveryService,
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
    CloudUserService provideCloudUserService(SplashActivity splashActivity) {
        return new FirebaseCloudUserService(splashActivity);
    }

    @Provides
    @ActivityScope
    CloudDeviceService provideCloudDeviceService(SplashActivity splashActivity) {
        return new FirebaseCloudDeviceService();
    }
}
