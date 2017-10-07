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
import eu.geekhome.asymptote.services.ObfuscatorService;
import eu.geekhome.asymptote.services.PasswordService;
import eu.geekhome.asymptote.services.PrivacyService;
import eu.geekhome.asymptote.services.UserMessageAcknowledgeService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.WiFiParamsResolver;
import eu.geekhome.asymptote.services.impl.AndroidWiFiHelper;
import eu.geekhome.asymptote.services.impl.AndroidWiFiParamsResolver;
import eu.geekhome.asymptote.services.impl.EfraespadaObfuscationService;
import eu.geekhome.asymptote.services.impl.FirebaseCertificateChecker;
import eu.geekhome.asymptote.services.impl.MemoryEmergencyManager;
import eu.geekhome.asymptote.services.impl.PreferencesPasswordService;
import eu.geekhome.asymptote.services.impl.PreferencesPrivacyService;
import eu.geekhome.asymptote.services.impl.PreferencesUserMessageAcknowledgeService;
import eu.geekhome.asymptote.services.impl.ResourcesBasedFirmwareRepository;

@SuppressWarnings("WeakerAccess")
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
    protected Context provideContext() {
        return _application;
    }

    @Provides
    @ApplicationScope
    protected FirmwareRepository provideFirmwareRepository() {
        return new ResourcesBasedFirmwareRepository();
    }

    @Provides
    @ApplicationScope
    protected EmergencyManager provideEmergencyManager() {
        return new MemoryEmergencyManager(false, null);
    }

    @Provides
    @ApplicationScope
    protected WiFiParamsResolver provideWiFiParamsResolver(WiFiHelper wiFiHelper) {
        return new AndroidWiFiParamsResolver(wiFiHelper);
    }

    @Provides
    @ApplicationScope
    protected WiFiHelper provideWiFiHelper() {
        return new AndroidWiFiHelper(_application);
    }

    @Provides
    @ApplicationScope
    protected PrivacyService providePrivacyService() {
        return new PreferencesPrivacyService(_application);
    }

    @Provides
    @ApplicationScope
    protected CloudCertificateChecker provideCloudChecker() {
        return new FirebaseCertificateChecker("https://asymptote-769eb.firebaseio.com");
    }

    @Provides
    @ApplicationScope
    protected UserMessageAcknowledgeService provideUserMessageAcknowledgeService() {
        return new PreferencesUserMessageAcknowledgeService(_application);
    }

    @Provides
    @ApplicationScope
    protected ObfuscatorService provideObfuscatorService() {
        return new EfraespadaObfuscationService(_application);
    }

    @Provides
    @ApplicationScope
    protected PasswordService providePasswordService(ObfuscatorService obfuscatorService) {
        return new PreferencesPasswordService(_application, obfuscatorService);
    }
}
