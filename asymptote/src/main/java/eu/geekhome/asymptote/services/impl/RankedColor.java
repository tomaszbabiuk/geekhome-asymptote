package eu.geekhome.asymptote.services.impl;

import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Hashtable;
import java.util.TreeMap;

public class RankedColor {
    @SerializedName("color")
    private final int _color;

    @SerializedName("rank")
    private int _rank;

    public RankedColor(int color) {
        _color = color;
        _rank = 1;
    }

    public int getColor() {
        return _color;
    }

    public int getRank() {
        return _rank;
    }

    public void incrementRank() {
        _rank++;
    }
}
