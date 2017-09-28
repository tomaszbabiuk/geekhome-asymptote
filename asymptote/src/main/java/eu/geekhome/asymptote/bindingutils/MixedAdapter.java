package eu.geekhome.asymptote.bindingutils;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import java.util.List;

import eu.geekhome.asymptote.BR;

public class MixedAdapter extends BaseDataBoundAdapter {
    private ObservableArrayList<LayoutHolder> mItems = new ObservableArrayList<>();

    public MixedAdapter(ObservableArrayList<LayoutHolder> items) {
        mItems = items;
        mItems.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<LayoutHolder>>() {
            @Override
            public void onChanged(ObservableList<LayoutHolder> layoutHolders) {

            }

            @Override
            public void onItemRangeChanged(ObservableList<LayoutHolder> layoutHolders, int i, int i1) {

            }

            @Override
            public void onItemRangeInserted(ObservableList<LayoutHolder> layoutHolders, int i, int i1) {
                notifyItemInserted(i);
            }

            @Override
            public void onItemRangeMoved(ObservableList<LayoutHolder> layoutHolders, int i, int i1, int i2) {

            }

            @Override
            public void onItemRangeRemoved(ObservableList<LayoutHolder> layoutHolders, int i, int i1) {

            }
        });
    }

    @Override
    protected void bindItem(DataBoundViewHolder holder, int position, List payloads) {
        LayoutHolder item = mItems.get(position);
        item.onBinding(holder.binding);
        holder.binding.setVariable(BR.vm, item);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemLayoutId(int position) {
        LayoutHolder item = getItem(position);
        return item.getItemLayoutId();
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public LayoutHolder getItem(int position) {
        return mItems.get(position);
    }
}
