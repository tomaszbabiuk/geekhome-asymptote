package eu.geekhome.asymptote.bindingutils;

import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;

@SuppressWarnings("unused")
public class BindingAdapters {
    @BindingAdapter({"items"})
    public static void setItems(View view, ObservableArrayList<LayoutHolder> items) {
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView)view;
            recyclerView.setAdapter(new MixedAdapter(items));
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"items"})
    public static void setItems(Spinner spinner, ArrayList<String> items) {
        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<>(spinner.getContext(), android.R.layout.simple_list_item_1, items);
        spinner.setAdapter(itemsAdapter);
    }
}
