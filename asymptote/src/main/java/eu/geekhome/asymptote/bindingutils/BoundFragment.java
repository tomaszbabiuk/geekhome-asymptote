package eu.geekhome.asymptote.bindingutils;

import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoundFragment extends Fragment {
    private static final Logger _logger = LoggerFactory.getLogger(BoundFragment.class);

    private ViewModel _model;
    private ViewParam[] _params;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (_model != null) {
            ViewDataBinding binding = _model.createBinding(inflater, container);
            _model.setBinding(binding);
            return binding.getRoot();
        } else {
            _logger.debug("Creating view FAILED for: %s " + getClass().getSimpleName());
            return null;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyViewParams();
    }

    private void applyViewParams() {
        if (_params != null) {
            for (ViewParam param : _params) {
                try {
                    param.apply(getActivity(), getView());
                } catch (Exception e) {
                    _logger.error("Error applying view params: " + e.toString());
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (_model != null) {
            _model.onResume();
        } else {
            _logger.debug("Resuming view FAILED for: %s " + getClass().getSimpleName());
        }
    }

    @Override
    public void onPause() {
        if (_model != null) {
            _model.onPause();
        } else {
            _logger.debug("Pausing view FAILED for: %s " + getClass().getSimpleName());
        }

        super.onPause();
    }

    public void setModel(ViewModel model) {
        _model = model;
    }

    public ViewModel getModel() {
        return _model;
    }

    public void setViewParams(ViewParam... params) {
        _params = params;
    }

    public boolean goingBack() {
        return _model == null || _model.goingBack();
    }
}
