package com.thoughtworks.orteroid.utilities;

import android.app.Activity;
import android.graphics.Typeface;

public class Font {
    public static Typeface setFont(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Delius-Regular.ttf");
    }
    public static Typeface setFontForIdea(Activity activity) {
        return Typeface.createFromAsset(activity.getAssets(), "fonts/Handlee-Regular.ttf");
    }

}
