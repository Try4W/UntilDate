package org.flycraft.android.untildate.utils;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class ToolbarColorizer {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void colorizeTo(final Window window, final int targetColor) {
        final int currentColor = window.getStatusBarColor();

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Use animation position to blend colors.
                float position = animation.getAnimatedFraction();

                // Apply blended color to the status bar.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int blended = blendColors(currentColor, targetColor, position);
                    window.setStatusBarColor(blended);
                }
            }
        });

        anim.setDuration(500).start();
    }
    public static void colorizeTo(final Toolbar toolbar, final int targetColor) {
        final int currentColor = ViewUtils.getBackgroundColor(toolbar);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float position = animation.getAnimatedFraction();

                int blendedColor = blendColors(currentColor, targetColor, position);
                toolbar.setBackgroundColor(blendedColor);
            }
        });

        anim.setDuration(500).start();
    }

    public static void colorizeTo(final AppBarLayout appBar, final int targetColor) {
        final int currentColor = ViewUtils.getBackgroundColor(appBar);

        ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float position = animation.getAnimatedFraction();

                int blendedColor = blendColors(currentColor, targetColor, position);
                ColorDrawable drawable = new ColorDrawable(blendedColor);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    appBar.setBackgroundDrawable(drawable);
                } else {
                    appBar.setBackground(drawable);
                }
            }
        });

        anim.setDuration(500).start();
    }

    private static int blendColors(int from, int to, float ratio) {
        final float inverseRatio = 1f - ratio;

        final float r = Color.red(to) * ratio + Color.red(from) * inverseRatio;
        final float g = Color.green(to) * ratio + Color.green(from) * inverseRatio;
        final float b = Color.blue(to) * ratio + Color.blue(from) * inverseRatio;

        return Color.rgb((int) r, (int) g, (int) b);
    }


}
