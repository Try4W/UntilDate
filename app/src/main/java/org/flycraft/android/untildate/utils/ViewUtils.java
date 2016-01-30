package org.flycraft.android.untildate.utils;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ViewUtils {

    public static int getBackgroundColor(View view) {
        Drawable background = view.getBackground();
        if(background == null) {
            throw new NullPointerException("background is null");
        }
        if(background instanceof ColorDrawable) {
            final int backgroundColor = ((ColorDrawable) background).getColor();
            return backgroundColor;
        } else {
            throw new RuntimeException("background isn't ColorDrawable");
        }
    }

}
