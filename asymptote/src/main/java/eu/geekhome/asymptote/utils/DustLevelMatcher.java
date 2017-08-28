package eu.geekhome.asymptote.utils;

import android.support.annotation.DrawableRes;

import eu.geekhome.asymptote.R;

public class DustLevelMatcher {
    @DrawableRes
    public static int matchLevel(Integer value, int level) {
        if (value != null) {
            if (level == 1) {
                if (value < 1500) {
                    return R.drawable.rectangle_dust_level1_selected;
                } else {
                    return R.drawable.rectangle_dust_level1;
                }
            }

            if (level == 2) {
                if (value >= 1500 && value < 3000) {
                    return R.drawable.rectangle_dust_level2_selected;
                } else {
                    return R.drawable.rectangle_dust_level2;
                }
            }

            if (level == 3) {
                if (value >= 3000 && value < 4500) {
                    return R.drawable.rectangle_dust_level3_selected;
                } else {
                    return R.drawable.rectangle_dust_level3;
                }
            }

            if (level == 4) {
                if (value >= 4500 && value < 6000) {
                    return R.drawable.rectangle_dust_level4_selected;
                } else {
                    return R.drawable.rectangle_dust_level4;
                }
            }

            if (level == 5) {
                if (value >= 6000 && value < 7500) {
                    return R.drawable.rectangle_dust_level5_selected;
                } else {
                    return R.drawable.rectangle_dust_level5;
                }
            }

            if (level == 6) {
                if (value >= 7500 && value < 9000) {
                    return R.drawable.rectangle_dust_level6_selected;
                } else {
                    return R.drawable.rectangle_dust_level6;
                }
            }

            if (level == 7) {
                if (value >= 9000 && value < 10500) {
                    return R.drawable.rectangle_dust_level7_selected;
                } else {
                    return R.drawable.rectangle_dust_level7;
                }
            }

            if (level == 8) {
                if (value >= 10500 && value < 12000) {
                    return R.drawable.rectangle_dust_level8_selected;
                } else {
                    return R.drawable.rectangle_dust_level8;
                }
            }

            if (level == 9) {
                if (value >= 12000) {
                    return R.drawable.rectangle_dust_level9_selected;
                } else {
                    return R.drawable.rectangle_dust_level9;
                }
            }
        }

        return R.drawable.rectangle_dust_level1;
    }
}
