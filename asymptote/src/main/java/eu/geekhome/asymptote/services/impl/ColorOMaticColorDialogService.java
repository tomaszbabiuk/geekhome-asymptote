package eu.geekhome.asymptote.services.impl;

import android.content.Context;
import android.content.DialogInterface;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import eu.geekhome.asymptote.R;
import eu.geekhome.asymptote.services.ColorDialogService;
import eu.geekhome.asymptote.services.ColorPickedListener;

public class ColorOMaticColorDialogService implements ColorDialogService {
    private final Context _context;

    public ColorOMaticColorDialogService(Context context) {
        _context = context;
    }

    public void pick(final int currentColor, final ColorPickedListener colorPickedListener) {
        ColorPickerDialogBuilder
                .with(_context)
                .setTitle(_context.getString(R.string.choose_color))
                .initialColor(currentColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(12)
                .lightnessSliderOnly()
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                    }
                })
                .setPositiveButton(_context.getString(R.string.choose), new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        colorPickedListener.colorPicked(selectedColor);
                    }
                })
                .setNegativeButton(_context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}
