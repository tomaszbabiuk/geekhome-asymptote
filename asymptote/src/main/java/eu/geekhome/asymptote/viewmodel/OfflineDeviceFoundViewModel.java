package eu.geekhome.asymptote.viewmodel;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;

import java.net.InetAddress;

import javax.inject.Inject;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;
import eu.geekhome.asymptote.dependencyinjection.activity.ActivityComponent;
import eu.geekhome.asymptote.services.UdpService;

public class OfflineDeviceFoundViewModel extends BaseObservable implements LayoutHolder {

    private final String _fingerprint;
    private String _restoreToken;
    private InetAddress _address;

    @Inject
    UdpService _udpService;

    @Inject
    Context _context;

    public OfflineDeviceFoundViewModel(ActivityComponent activityComponent, InetAddress address, String restoreToken) {
        activityComponent.inject(this);
        _address = address;
        _restoreToken = restoreToken;
        _fingerprint = _context.getString(R.string.cloud_fingerprint);
    }

    public void updateCertificate() {
        _udpService.requestCertRestore(getAddress(), _restoreToken, _fingerprint);

    }

    @Override
    public int getItemLayoutId() {
        return R.layout.offline_device_found;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
    }

    @Bindable
    public InetAddress getAddress() {
        return _address;
    }

    public void setAddress(InetAddress address) {
        _address = address;
        notifyPropertyChanged(BR.addresses);
    }
}
