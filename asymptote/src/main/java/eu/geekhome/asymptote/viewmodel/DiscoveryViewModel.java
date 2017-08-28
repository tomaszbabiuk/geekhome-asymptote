package eu.geekhome.asymptote.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import eu.geekhome.asymptote.BR;
import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.bindingutils.LayoutHolder;

public class DiscoveryViewModel extends BaseObservable implements LayoutHolder {

    private String _timingMessage;

    public DiscoveryViewModel() {
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.listitem_discovery;
    }

    @Override
    public void onBinding(ViewDataBinding binding) {
        View clockBig = binding.getRoot().findViewById(R.id.clock_bighand);
        View clockSmall = binding.getRoot().findViewById(R.id.clock_smallhand);
        Animation rotateBigAnimation = AnimationUtils.loadAnimation(clockBig.getContext(), R.anim.rotate_around_center_point_linear);
        clockBig.startAnimation(rotateBigAnimation);
        Animation rotateSmallAnimation = AnimationUtils.loadAnimation(clockSmall.getContext(), R.anim.rotate_around_center_point_linear_fast);
        clockSmall.startAnimation(rotateSmallAnimation);
    }

    @Bindable
    public String getTimingMessage() {
        return _timingMessage;
    }

    public void setTimingMessage(String timingMessage) {
        _timingMessage = timingMessage;
        notifyPropertyChanged(BR.timingMessage);
    }
}
