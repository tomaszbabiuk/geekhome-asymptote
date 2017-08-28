package eu.geekhome.asymptote.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;

import java.net.InetAddress;
import java.util.List;

import javax.inject.Inject;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentTouchProgressBinding;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.NavigationService;

public class TouchProgressViewModel extends HelpViewModelBase<FragmentTouchProgressBinding> {
    private boolean _blocked;
    private boolean _hasResult;
    private final WiFiParameters _params;
    private Runnable _cancelRunnable;

    @Inject
    NavigationService _navigationService;
    @Inject
    AddressesPersistenceService _addressesPersistenceService;
    @Inject
    Context _context;

    public TouchProgressViewModel(ActivityComponent activityComponent, WiFiParameters params) {
        super(activityComponent);
        _params = params;
        _hasResult = false;
    }

    @Override
    protected String getNoWiFiRationale() {
        return _context.getString(R.string.rationale_nowifi_adding_devices);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!_hasResult) {
            EspTouchAsyncTask asyncTask = new EspTouchAsyncTask();
            asyncTask.execute();
        }

        Animation rotateAnimation = AnimationUtils.loadAnimation(_context, R.anim.rotate_around_center_point_linear_fast);
        getBinding().imageRefreshOuter.startAnimation(rotateAnimation);
    }


    public void onCancel(@NonNull View view) {
        if (_cancelRunnable != null) {
            _cancelRunnable.run();
        }

        _navigationService.goBack();
        _navigationService.goBack();
        _navigationService.goBack();
    }

    @Override
    public FragmentTouchProgressBinding createBinding(LayoutInflater inflater, ViewGroup container) {
        FragmentTouchProgressBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_touch_progress, container, false);
        binding.setVm(this);
        return binding;
    }

    @Override
    protected void doInject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Bindable
    public boolean isBlocked() {
        return _blocked;
    }

    public void setBlocked(boolean blocked) {
        _blocked = blocked;
    }


    private class EspTouchAsyncTask extends AsyncTask<Void, Void, List<IEsptouchResult>> {
        @Override
        protected void onPreExecute() {
            _hasResult = false;
        }

        @Override
        protected List<IEsptouchResult> doInBackground(Void... voids) {
            final EsptouchTask esptouchTask = new EsptouchTask(_params.getSsid(), _params.getBssid(), _params.getPassword(), _params.isHidden(), _context);
            blockInterface(new Runnable() {
                @Override
                public void run() {
                    if (!esptouchTask.isCancelled()) {
                        esptouchTask.interrupt();
                    }
                    setBlocked(false);
                }
            });
            setBlocked(true);
            return esptouchTask.executeForResults(1);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            if (!_hasResult) {
                _hasResult = true;

                IEsptouchResult firstResult = result.get(0);
                if (!firstResult.isCancelled()) {
                    releaseInterface();
                    if (firstResult.isSuc()) {
                        processSuccess(firstResult.getInetAddress());
                    } else {
                        processFailure();
                    }
                }
            }

            if (_cancelRunnable != null) {
                _cancelRunnable.run();
            }
        }
    }

    @Override
    public boolean goingBack() {
        if (_cancelRunnable != null) {
            _cancelRunnable.run();
        }
        return true;
    }

    public void blockInterface(Runnable cancelRunnable) {
        _cancelRunnable = cancelRunnable;
        setBlocked(true);
    }

    public void releaseInterface() {
        getBinding().imageRefreshOuter.clearAnimation();
        setBlocked(false);
    }

    private void processFailure() {
        String status = _context.getString(R.string.pairing_failed);
        String title = _context.getString(R.string.pair_device);
        ResultViewModel model = new ResultViewModel(getActivityComponent(), title, status, false);
        _navigationService.goBackTo(MainViewModel.class);
        _navigationService.showViewModel(model);
    }

    private void processSuccess(InetAddress inetAddress) {
        _addressesPersistenceService.add(inetAddress);

        String status = String.format(_context.getString(R.string.device_paired), inetAddress);
        String title = _context.getString(R.string.pair_device);
        ResultViewModel model = new ResultViewModel(getActivityComponent(), title, status, true);
        _navigationService.goBackTo(MainViewModel.class);
        _navigationService.showViewModel(model);
    }
}