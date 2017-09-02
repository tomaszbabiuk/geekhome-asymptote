package eu.geekhome.asymptote.dependencyinjection.activity;

import dagger.Module;
import dagger.Provides;
import eu.geekhome.asymptote.MainActivity;
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
import eu.geekhome.asymptote.services.impl.UdpLocalDiscoveryService;

@Module
public class MainActivityModule {
    @Provides
    @ActivityScope
    ToastService provideToastService(MainActivity mainActivity) {
        return new AndroidToastService(mainActivity);
    }

    @Provides
    @ActivityScope
    AddressesPersistenceService provideAddressesPersistenceService(MainActivity mainActivity) {
        return new PreferencesAddressesPersistenceService(mainActivity);
    }

    @Provides
    @ActivityScope
    FavoriteColorsService provideFavoriteColorsService(MainActivity mainActivity) {
        return new PreferencesFavoriteColorsService(mainActivity);
    }

    @Provides
    @ActivityScope
    NavigationService provideNavigationService(MainActivity mainActivity) {
        return new FragmentBasedNavigationService(mainActivity.getSupportFragmentManager(), mainActivity);
    }

    @Provides
    @ActivityScope
    ThreadRunner provideThreadRunner(MainActivity mainActivity) {
        return new AndroidThreadRunner(mainActivity);
    }

    @Provides
    @ActivityScope
    UdpService provideUdpService(MainActivity mainActivity, ThreadRunner threadRunner) {
        return new AndroidUdpService(mainActivity, threadRunner);
    }

    @Provides
    @ActivityScope
    ColorDialogService provideColorDialogService(MainActivity mainActivity) {
        return new ColorOMaticColorDialogService(mainActivity);
    }

    @Provides
    @ActivityScope
    LocalDiscoveryService provideLocalDiscoveryService(UdpService udpService) {
        return new UdpLocalDiscoveryService(udpService);
    }

    @Provides
    @ActivityScope
    GeneralDialogService provideGeneralDialogService(MainActivity mainActivity) {
        return new AndroidGeneralDialogService(mainActivity, mainActivity.getFragmentManager());
    }

    @Provides
    @ActivityScope
    OtaServer provideOtaServer(MainActivity mainActivity) {
        try {
            return new NanoOtaServer(mainActivity.getApplicationContext());
        } catch (Exception e) {
            return new BrokenOtaServer();
        }
    }

    @Provides
    @ActivityScope
    SyncManager provideSyncManager(MainActivity activity, LocalDiscoveryService localDiscoveryService,
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
    CloudUserService provideCloudUserService(MainActivity mainActivity) {
        return new FirebaseCloudUserService(mainActivity);
    }

    @Provides
    @ActivityScope
    CloudDeviceService provideCloudDeviceService() {
        return new FirebaseCloudDeviceService();
    }
}
