package eu.geekhome.asymptote.dependencyinjection.application;

import android.app.Application;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import eu.geekhome.asymptote.services.CloudDeviceService;
import eu.geekhome.asymptote.services.CloudUserService;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.FirmwareRepository;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.WiFiParamsResolver;
import eu.geekhome.asymptote.services.impl.AndroidWiFiHelper;
import eu.geekhome.asymptote.services.impl.AndroidWiFiParamsResolver;
import eu.geekhome.asymptote.services.impl.FirebaseCloudDeviceService;
import eu.geekhome.asymptote.services.impl.FirebaseCloudUserService;
import eu.geekhome.asymptote.services.impl.MemoryEmergencyManager;
import eu.geekhome.asymptote.services.impl.ResourcesBasedFirmwareRepository;

@Module
public class ApplicationModule {
    private Application _application;

    public ApplicationModule(Application application) {
        _application = application;
    }

    @Provides
    @ApplicationScope
    Application provideApplication() {
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

}
