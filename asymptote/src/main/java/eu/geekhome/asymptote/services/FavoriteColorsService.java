package eu.geekhome.asymptote.services;

import java.util.ArrayList;

import eu.geekhome.asymptote.model.DeviceKey;
import eu.geekhome.asymptote.services.impl.RankedColor;

public interface FavoriteColorsService {
    void colorPicked(String setId, int color);
    void colorRemoved(String setId, int color);
    ArrayList<RankedColor> getFavoriteColors(String setId);
}
