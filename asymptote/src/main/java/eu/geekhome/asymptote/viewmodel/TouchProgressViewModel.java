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

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.databinding.FragmentTouchProgressBinding;
import eu.geekhome.asymptote.model.WiFiParameters;
import eu.geekhome.asymptote.services.AddressesPersistenceService;
import eu.geekhome.asymptote.services.NavigationService;
import eu.geekhome.asymptote.services.WiFiHelper;
import eu.geekhome.asymptote.services.impl.MainViewModelsFactory;

public class TouchProgressViewModel extends HelpViewModelBase<FragmentTouchProgressBinding> {
    private final MainViewModelsFactory _factory;
    private boolean _blocked;
    private boolean _hasResult;
    private final WiFiParameters _params;
    private Runnable _cancelRunnable;

    private final NavigationService _navigationService;
    private final AddressesPersistenceService _addressesPersistenceService;
    private final Context _context;

    public TouchProgressViewModel(Context context, MainViewModelsFactory factory,
                                  NavigationService navigationService, WiFiHelper wifiHelper,
                                  AddressesPersistenceService addressesPersistenceService, WiFiParameters params) {
        super(factory, wifiHelper, navigationService);
        _factory = factory;
        _params = params;
        _hasResult = false;
        _context = context;
        _addressesPersistenceService = addressesPersistenceService;
        _navigationService = navigationService;
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

    private void blockInterface(Runnable cancelRunnable) {
        _cancelRunnable = cancelRunnable;
        setBlocked(true);
    }

    private void releaseInterface() {
        getBinding().imageRefreshOuter.clearAnimation();
        setBlocked(false);
    }

    private void processFailure() {
        String status = _context.getString(R.string.pairing_failed);
        String title = _context.getString(R.string.pair_device);
        ResultViewModel model = _factory.createResultViewModel(title, status, false);
        _navigationService.goBackTo(MainViewModel.class);
        _navigationService.showViewModel(model);
    }

    private void processSuccess(InetAddress inetAddress) {
        _addressesPersistenceService.add(inetAddress);

        String status = String.format(_context.getString(R.string.device_paired), inetAddress);
        String title = _context.getString(R.string.pair_device);
        ResultViewModel model = _factory.createResultViewModel(title, status, true);
        _navigationService.goBackTo(MainViewModel.class);
        _navigationService.showViewModel(model);
    }
}