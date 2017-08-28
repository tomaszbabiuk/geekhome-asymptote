package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ColorsSet {
    @SerializedName("key")
    private String _key;

    @SerializedName("role")
    private ArrayList<RankedColor> _rankedColors;

    public ColorsSet() {
        _rankedColors = new ArrayList<>();
    }

    public ColorsSet(String key) {
        this();
        _key = key;
    }

    public String getKey() {
        return _key;
    }

    public void rankColor(int color) {
        for (RankedColor rankedColor : _rankedColors) {
            if (rankedColor.getColor() == color) {
                rankedColor.incrementRank();
                return;
            }
        }

        _rankedColors.add(new RankedColor(color));
    }

    public ArrayList<RankedColor> getRankedColors() {
        return _rankedColors;
    }

    public ArrayList<RankedColor> sort() {
        Collections.sort(_rankedColors, new Comparator<RankedColor>() {
            @Override
            public int compare(RankedColor o1, RankedColor o2) {
                return o2.getRank() - o1.getRank();
            }
        });

        return _rankedColors;
    }
}
