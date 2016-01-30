package org.flycraft.android.untildate.fragments.other;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.activities.EditNoteActivity;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.fragments.NotesListFragment;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "NoteViewHolder";
    private static final String handler_message_key = "timeLeftText";

    private CardView view;
    private TextView titleField;
    private TextView timeLeftField;

    private DateTime noteDate;

    private Handler updateHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String timeLeftText = msg.getData().getString(handler_message_key);
            timeLeftField.setText(timeLeftText);
            return true;
        }
    });

    private ScheduledExecutorService schExecutor = Executors.newSingleThreadScheduledExecutor();

    public NoteViewHolder(View view) {
        super(view);

        this.view = (CardView) view;
        this.titleField = (TextView) view.findViewById(R.id.title);
        this.timeLeftField = (TextView) view.findViewById(R.id.time_left);

        setupUpdater();
    }

    private void setupUpdater() {
        schExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                updateCounter();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void updateCounter() {
        Resources res = view.getResources();
        Message message = new Message();
        Bundle bundle = new Bundle();
        if(noteDate.isAfterNow()) {
            DateTime currentDate = DateTime.now();
            Period period = new Period(currentDate, noteDate);

            String timeLeftMessage = "";

            int days = Days.daysBetween(currentDate, noteDate).getDays();
            int hours = period.getHours();
            int minutes = period.getMinutes();
            int seconds = period.getSeconds();
            //int mills = period.getMillis();
            if (days > 0) {
                timeLeftMessage += res.getQuantityString(R.plurals.days, days, days) + "\n";
            }
            if (hours > 0) {
                timeLeftMessage += res.getQuantityString(R.plurals.hours, hours, hours) + " ";
            }
            timeLeftMessage += res.getQuantityString(R.plurals.minutes, minutes, minutes) + " ";
            timeLeftMessage += res.getQuantityString(R.plurals.seconds, seconds, seconds) + " ";
            //timeLeftMessage += mills;
            bundle.putString(handler_message_key, timeLeftMessage);
        } else {
            bundle.putString(handler_message_key, res.getString(R.string.date_occurred));
        }

        message.setData(bundle);
        updateHandler.sendMessage(message);
    }

    public void setData(Note note) {
        setData(note.getTitle(), note.getDate(), note.getColor());
    }

    private void setData(String title, DateTime noteDate, int backgroundColor) {
        this.titleField.setText(title);
        this.noteDate = noteDate;
        this.view.setCardBackgroundColor(backgroundColor);
        updateCounter();
    }

    public View getView() {
        return view;
    }

}
