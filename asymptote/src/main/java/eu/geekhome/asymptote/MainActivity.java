package eu.geekhome.asymptote;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import eu.geekhome.asymptote.model.DeviceSnapshot;
import eu.geekhome.asymptote.model.UserSnapshot;
import eu.geekhome.asymptote.services.EmergencyManager;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;
import eu.geekhome.asymptote.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    public static final String INTENT_USER_ID = "INTENT_USER_ID";
    public static final String INTENT_EMERGENCY = "INTENT_EMERGENCY";
    public static final String INTENT_EMERGENCY_PASSWORD = "INTENT_EMERGENCY_PASSWORD";
    public static final String INTENT_DEVICE_SNAPSHOTS = "INTENT_DEVICE_SNAPSHOTS";

    @Inject
    NavigationService _navigationService;

    @Inject
    EmergencyManager _emergencyManager;

    @Inject
    MainViewModelsFactory _factory;

    public static Intent createOpeningIntent(Context context, String userId, boolean emergency,
                                             UserSnapshot userSnapshot) {
        Intent result = new Intent(context, MainActivity.class);
        result.putExtra(INTENT_USER_ID, userId);
        result.putExtra(INTENT_EMERGENCY, emergency);
        if (userSnapshot != null) {
            result.putExtra(INTENT_EMERGENCY_PASSWORD, userSnapshot.getEmergencyPassword());
            if (userSnapshot.getDeviceSnapshots() != null) {
                ArrayList<String> snapshotsSerialized = new ArrayList<>();
                for (DeviceSnapshot snapshot : userSnapshot.getDeviceSnapshots()) {
                    snapshotsSerialized.add(snapshot.toString());
                }
                result.putStringArrayListExtra(INTENT_DEVICE_SNAPSHOTS, snapshotsSerialized);
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        setContentView(R.layout.activity_stack);

        String userId = getIntent().getStringExtra(INTENT_USER_ID);
        boolean emergency = getIntent().getBooleanExtra(INTENT_EMERGENCY, false);
        String emergencyPassword = getIntent().getStringExtra(INTENT_EMERGENCY_PASSWORD);
        ArrayList<String> deviceSnapshotsArray = getIntent().getStringArrayListExtra(INTENT_DEVICE_SNAPSHOTS);
        ArrayList<DeviceSnapshot> deviceSnapshots = new ArrayList<>();
        try {
            for (String deviceSnapshotSerialized : deviceSnapshotsArray) {
                deviceSnapshots.add(new DeviceSnapshot(deviceSnapshotSerialized));
            }
        } catch (Exception ex){
            deviceSnapshots = null;
        }
        UserSnapshot userSnapshot = new UserSnapshot(deviceSnapshots, emergencyPassword);

        _emergencyManager.setEmergency(emergency);
        _emergencyManager.setPassword(userSnapshot.getEmergencyPassword());
        MainViewModel mainViewModel = _factory.createMainViewModel(userId, userSnapshot);
        _navigationService.showViewModel(mainViewModel);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finishAffinity();
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            if (_navigationService.goingBack()) {
                getSupportFragmentManager().popBackStack();
            }
        } else {
            super.onBackPressed();
        }
    }
}
