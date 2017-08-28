package eu.geekhome.asymptote.bindingutils.controls;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public abstract class RecyclerViewWithLayoutManager extends RecyclerView {
    public RecyclerViewWithLayoutManager(Context context) {
        super(context);
        initLayoutManager();
    }

    public RecyclerViewWithLayoutManager(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayoutManager();
    }

    public RecyclerViewWithLayoutManager(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initLayoutManager();
    }

    protected abstract void initLayoutManager();
}
