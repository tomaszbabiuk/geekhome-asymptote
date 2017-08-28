package eu.geekhome.asymptote.bindingutils.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;

public class VerticalRecyclerView extends RecyclerViewWithLayoutManager {
    public VerticalRecyclerView(Context context) {
        super(context);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initLayoutManager() {
        setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.VERTICAL, false));
    }
}
