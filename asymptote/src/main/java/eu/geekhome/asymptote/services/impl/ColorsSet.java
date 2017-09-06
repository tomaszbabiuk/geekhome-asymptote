package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class ColorsSet {
    @SerializedName("key")
    private String _key;

    @SerializedName("colors")
    private ArrayList<RankedColor> _rankedColors;

    private ColorsSet() {
        _rankedColors = new ArrayList<>();
    }

    ColorsSet(String key) {
        this();
        _key = key;
    }

    public void remove(int color) {
        RankedColor toRemove = null;
        for (RankedColor rankedColor : _rankedColors) {
            if (rankedColor.getColor() == color) {
                toRemove = rankedColor;
                break;
            }
        }

        if (toRemove != null) {
            _rankedColors.remove(toRemove);
        }
    }

    public String getKey() {
        return _key;
    }

    void rankColor(int color) {
        for (RankedColor rankedColor : _rankedColors) {
            if (rankedColor.getColor() == color) {
                rankedColor.incrementRank();
                return;
            }
        }

        _rankedColors.add(new RankedColor(color));
    }

    ArrayList<RankedColor> getRankedColors() {
        return _rankedColors;
    }

    ArrayList<RankedColor> sort() {
        Collections.sort(_rankedColors, new Comparator<RankedColor>() {
            @Override
            public int compare(RankedColor o1, RankedColor o2) {
                return o2.getRank() - o1.getRank();
            }
        });

        return _rankedColors;
    }
}
