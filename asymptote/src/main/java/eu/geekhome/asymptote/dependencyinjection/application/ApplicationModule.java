package eu.geekhome.asymptote.dependencyinjection.application;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import eu.geekhome.asymptote.MainActivityComponent;
import eu.geekhome.asymptote.SplashActivityComponent;
import eu.geekhome.asymptote.services.CloudCertificateChecker;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.PrivacyService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.WiFiParamsResolver;
import eu.geekhome.asymptote.services.impl.AndroidWiFiHelper;
import eu.geekhome.asymptote.services.impl.AndroidWiFiParamsResolver;
import eu.geekhome.asymptote.services.impl.FirebaseCertificateChecker;
import eu.geekhome.asymptote.services.impl.MemoryEmergencyManager;
import eu.geekhome.asymptote.services.impl.PreferencesPrivacyService;
import eu.geekhome.asymptote.services.impl.ResourcesBasedFirmwareRepository;

@Module(subcomponents = {
        MainActivityComponent.class,
        SplashActivityComponent.class})
public class ApplicationModule {
    private Application _application;

    public ApplicationModule(Application application) {
        _application = application;
    }

    @Provides
    @ApplicationScope
    Context provideContext() {
        return _application;
    }

    @Provides
    @ApplicationScope
    FirmwareRepository provideFirmwareRepository() {
        return new ResourcesBasedFirmwareRepository();
    }

    @Provides
    @ApplicationScope
    EmergencyManager provideEmergencyManager() {
        return new MemoryEmergencyManager(false, null);
    }

    @Provides
    @ApplicationScope
    WiFiParamsResolver provideWiFiParamsResolver(WiFiHelper wiFiHelper) {
        return new AndroidWiFiParamsResolver(wiFiHelper);
    }

    @Provides
    @ApplicationScope
    WiFiHelper provideWiFiHelper() {
        return new AndroidWiFiHelper(_application);
    }

    @Provides
    @ApplicationScope
    PrivacyService providePrivacyService() {
        return new PreferencesPrivacyService(_application);
    }

    @Provides
    @ApplicationScope
    CloudCertificateChecker provideCloudChecker() {
        return new FirebaseCertificateChecker("https://asymptote-769eb.firebaseio.com");
    }
}
