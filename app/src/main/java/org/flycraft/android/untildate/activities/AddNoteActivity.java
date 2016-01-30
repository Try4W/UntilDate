package org.flycraft.android.untildate.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.woalk.apps.lib.colorpicker.ColorPickerDialog;
import com.woalk.apps.lib.colorpicker.ColorPickerSwatch;

import org.apache.commons.lang3.StringUtils;
import org.flycraft.android.untildate.NoteColors;
import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.data.NotesStorage;
import org.flycraft.android.untildate.utils.ToolbarColorizer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AddNoteActivity extends AppCompatActivity {

    private static final String TAG = "AddNoteActivity";

    private CoordinatorLayout coordinatorLayout;
    protected ActionBar actionBar;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    private TextInputLayout titleEditTextLayout;
    protected AppCompatEditText titleEditText;
    private Button pickDateButton;
    private TextView dateTextView;
    private View colorImageView;
    private TextView colorTextView;
    private Button pickColorButton;

    private Note note;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titleEditTextLayout = (TextInputLayout) findViewById(R.id.title_edit_text_layout);
        titleEditTextLayout.setErrorEnabled(true);
        titleEditText = (AppCompatEditText) findViewById(R.id.title_edit_text);
        pickDateButton = (Button) findViewById(R.id.pick_date_button);
        pickDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate();
            }
        });

        dateTextView = (TextView) findViewById(R.id.date_text);

        colorImageView = (View) findViewById(R.id.color_view);

        colorTextView = (TextView) findViewById(R.id.color_text);

        pickColorButton = (Button) findViewById(R.id.pick_color_button);
        pickColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickColor();
            }
        });

        actionBar = getSupportActionBar();
        if(actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // about back arrow
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
        } else {
            throw new NullPointerException("getSupportActionBar() returns null");
        }

        note = setupNote();
        setupFields();
    }

    protected Note setupNote() {
        return new Note(null, null, NoteColors.blue);
    }

    /**
     * Hacks with MonthOfYear because Android and JodaTime time systems have some differences
     */

    private void pickDate() {
        DateTime dateTime = note.getDate();
        if(dateTime == null) {
            dateTime = new DateTime();
        }
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTime newDateTime = new DateTime()
                        .withYear(year)
                        .withMonthOfYear(monthOfYear + 1)
                        .withDayOfMonth(dayOfMonth);
                pickTime(newDateTime);
            }
        }, dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        dialog.show();
    }

    private void pickTime(final DateTime dateTime) {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                DateTime newDateTime = dateTime
                        .withHourOfDay(hourOfDay)
                        .withMinuteOfHour(minute);
                setDate(newDateTime);
            }
        }, dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), true);
        dialog.show();
    }

    private void pickColor() {
        ColorPickerDialog dialog = ColorPickerDialog.newInstance(
                R.string.pick_color_title,
                new int[]{
                        NoteColors.blue,
                        NoteColors.indigo,
                        NoteColors.brown,
                        NoteColors.gray,
                        NoteColors.green,
                        NoteColors.orange
                },
                note.getColor(),
                3, // Number of columns
                ColorPickerDialog.SIZE_SMALL);

        dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                note.setColor(color);
                animateUiColor(color);
            }

        });

        dialog.show(getFragmentManager(), "ColorPickerDialog_tag"); // why need tag?
    }

    protected void setDate(DateTime date) {
        if(date.isBeforeNow()) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout,
                    getString(R.string.warning_date_in_past),
                    Snackbar.LENGTH_LONG
            );

            snackbar.show();
            return;
        }
        note.setDate(date);

        DateTimeFormatter dateDecoder = DateTimeFormat.forPattern("yyyy-MM-dd / HH:mm");

        dateTextView.setText(dateDecoder.print(date));
    }

    private void animateUiColor(int color) {
        ToolbarColorizer.colorizeTo(getWindow(), NoteColors.getDark(color));
        ToolbarColorizer.colorizeTo(toolbar, color);
        ToolbarColorizer.colorizeTo(appBarLayout, color);
        setColorImageViewColor(color);
        colorTextView.setText(NoteColors.getString(this, color));
    }

    private void setUiColor(int color) {
        toolbar.setBackgroundColor(color);
        appBarLayout.setBackgroundColor(color);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(NoteColors.getDark(color));
        }
        setColorImageViewColor(color);
        colorTextView.setText(NoteColors.getString(this, color));
    }

    private void setColorImageViewColor(int color) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.circle_view);
        if(drawable != null) {
            drawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY));
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                colorImageView.setBackgroundDrawable(drawable);
            } else {
                colorImageView.setBackground(drawable);
            }
        } else {
            throw new NullPointerException("getDrawable(R.drawable.circle_view) is null");
        }
    }

    private boolean checkTitleField() {
        return !StringUtils.isBlank(titleEditText.getText().toString());
    }

    private boolean checkDatePicked() {
        return note.getDate() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_activity, menu);
        return true;
    }

    protected void setupFields() {
        setUiColor(note.getColor());
    }

    protected void putDataToNote() {
        note.setTitle(titleEditText.getText().toString());
    }

    protected void save() {
        NotesStorage.getInstance().add(note);
    }

    protected void finishWithResult() {
        setResult(RESULT_OK);
        finish();
    }

    protected Note getNote() {
        return note;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_save_note:
                if(!checkTitleField()) {
                    titleEditTextLayout.setError(getString(R.string.warning_no_title));
                    return true;
                }
                if(!checkDatePicked()) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout,
                            getString(R.string.warning_no_date),
                            Snackbar.LENGTH_LONG
                    );
                    snackbar.show();
                    return true;
                }
                putDataToNote();
                save();
                finishWithResult();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActionModeStarted(ActionMode mode) {
        super.onActionModeStarted(mode);
        ToolbarColorizer.colorizeTo(getWindow(), NoteColors.gray_dark); // Set nice color for ActionMenu
    }

    @Override
    public void onActionModeFinished(ActionMode mode) {
        super.onActionModeFinished(mode);
        ToolbarColorizer.colorizeTo(getWindow(), NoteColors.getDark(note.getColor())); // Set normal color for selected
    }
}
