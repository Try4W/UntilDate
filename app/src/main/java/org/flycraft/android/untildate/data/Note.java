package org.flycraft.android.untildate.data;

import android.graphics.Color;

import org.joda.time.DateTime;

import java.util.Date;

public class Note {

    private String title;
    private DateTime date;
    private int color;

    public Note(String title, DateTime date, int color) {
        this.title = title;
        this.date = date;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
