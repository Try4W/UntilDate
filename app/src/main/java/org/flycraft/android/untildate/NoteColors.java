package org.flycraft.android.untildate;

import android.content.Context;
import android.support.v4.content.ContextCompat;

public class NoteColors {

    public static int gray;
    public static int gray_dark;
    public static int indigo;
    public static int indigo_dark;
    public static int brown;
    public static int brown_dark;
    public static int blue;
    public static int blue_dark;
    public static int green;
    public static int green_dark;
    public static int orange;
    public static int orange_dark;

    public static void init(Context context) {
        gray = ContextCompat.getColor(context, R.color.note_gray);
        gray_dark = ContextCompat.getColor(context, R.color.note_dark_gray);
        indigo = ContextCompat.getColor(context, R.color.note_indigo);
        indigo_dark = ContextCompat.getColor(context, R.color.note_dark_indigo);
        blue = ContextCompat.getColor(context, R.color.note_blue);
        blue_dark = ContextCompat.getColor(context, R.color.note_dark_blue);
        green = ContextCompat.getColor(context, R.color.note_green);
        green_dark = ContextCompat.getColor(context, R.color.note_dark_green);
        brown = ContextCompat.getColor(context, R.color.note_brown);
        brown_dark = ContextCompat.getColor(context, R.color.note_dark_brown);
        orange = ContextCompat.getColor(context, R.color.note_orange);
        orange_dark = ContextCompat.getColor(context, R.color.note_dark_orange);
    }

    public static int getDark(int color) {
        if(color == gray) {
            return gray_dark;
        } else if (color == indigo) {
            return indigo_dark;
        } else if (color == brown) {
            return brown_dark;
        } else if (color == blue) {
            return blue_dark;
        } else if (color == green) {
            return green_dark;
        } else if (color == orange) {
            return orange_dark;
        }
        throw new RuntimeException("Wrong color!");
    }

    public static String getString(Context context, int color) {
        if(color == gray) {
            return context.getString(R.string.gray);
        } else if (color == indigo) {
            return context.getString(R.string.indigo);
        } else if (color == brown) {
            return context.getString(R.string.brown);
        } else if (color == blue) {
            return context.getString(R.string.blue);
        } else if (color == green) {
            return context.getString(R.string.green);
        } else if (color == orange) {
            return context.getString(R.string.orange);
        }
        throw new RuntimeException("Wrong color!");
    }

}
