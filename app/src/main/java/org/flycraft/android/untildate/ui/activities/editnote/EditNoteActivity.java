package org.flycraft.android.untildate.ui.activities.editnote;

import android.content.Intent;
import android.os.Bundle;

import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.data.NotesStorage;
import org.flycraft.android.untildate.ui.activities.addnote.AddNewNoteActivity;

public class EditNoteActivity extends AddNewNoteActivity {

    public static final String NOTE_POS_KEY = "notePos";

    private final NotesStorage storage = NotesStorage.getInstance();
    private int noteIndex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFields();
        actionBar.setTitle(R.string.toolbar_edit_title);
    }

    @Override
    protected Note setupNote() {
        Bundle bundle = getIntent().getExtras();
        if(bundle == null) {
            throw new NullPointerException("No arguments for EditNoteActivity");
        }
        noteIndex = bundle.getInt(NOTE_POS_KEY);
        return storage.getNotes().get(noteIndex);
    }

    @Override
    protected void setupFields() {
        super.setupFields();
        titleEditText.setText(getNote().getTitle());
        setDate(getNote().getDate());
    }

    @Override
    protected void putDataToNote() {
        super.putDataToNote();
        // All data already putted
    }

    @Override
    protected void save() {
        storage.saveData();
    }

    @Override
    protected void finishWithResult() {
        Bundle data = new Bundle();
        data.putInt(NOTE_POS_KEY, noteIndex);
        Intent intent = new Intent();
        intent.putExtras(data);
        setResult(RESULT_OK, intent);
        finish();
    }
}
