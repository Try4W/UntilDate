package org.flycraft.android.untildate.ui.fragments;

import android.os.storage.StorageManager;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import org.flycraft.android.untildate.NoteColors;
import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.data.NotesStorage;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

public class NotesListFragmentPresenter extends MvpBasePresenter<NotesListFragmentView> {

    private NotesStorage storage = NotesStorage.getInstance();

    private Note lastRemovedNote;
    private int lastRemovedNotePosition;

    private boolean currentGapState;

    public void initialize() {
        if(isViewAttached()) {
            getView().setNotesListData(storage.getNotes());
            forceUpdateGapState(); // for first time
        }
    }

    public void noteClick(int notePosition) {
        if(isViewAttached()) {
            getView().openEditNoteActivity(notePosition);
        }
    }

    public void dismissNote(int notePosition) {
        List<Note> globalData = storage.getNotes();

        lastRemovedNotePosition = notePosition;
        lastRemovedNote = globalData.remove(notePosition);

        updateGapState();
        storage.saveData();
        if(isViewAttached()) {
            getView().removeNote(notePosition);
            getView().showUndoSnackbar();
        }
    }

    public void moveNote(int fromPosition, int toPosition) {
        List<Note> globalData = storage.getNotes();
        Collections.swap(globalData, fromPosition, toPosition);
        storage.saveData();
        if(isViewAttached()) {
            getView().moveNote(fromPosition, toPosition);
        }
    }

    public void newNoteAdded() {
        if(isViewAttached()) {
            Note note = storage.getNotes().get(0); // this note was added in AddNewNoteActivity
            getView().appendNote(note);
            updateGapState();
        }
    }

    public void noteEdited(int position) {
        if(isViewAttached()) {
            getView().updateNote(position);
        }
    }

    public void undoRemoveAction() {
        storage.getNotes().add(lastRemovedNotePosition, lastRemovedNote);
        storage.saveData();
        if(isViewAttached()) {
            if (lastRemovedNote == null) {
                throw new RuntimeException("There is no actions to undo");
            }
            getView().insertNote(lastRemovedNotePosition, lastRemovedNote);
            lastRemovedNote = null;
            updateGapState();
        }
    }

    private void updateGapState() {
        if(isViewAttached()) {
            boolean gapState = storage.getNotes().isEmpty();
            if(currentGapState != gapState) {
                getView().setGapState(gapState);
                currentGapState = gapState;
            }
        }
    }

    private void forceUpdateGapState() {
        if(isViewAttached()) {
            currentGapState = storage.getNotes().isEmpty();
            getView().setGapState(currentGapState);
        }
    }

    public void tryToAddDemoNotesSet() {
        if(storage.getNotes().isEmpty()) {
            List<Note> notes = storage.getNotes();
            String[] demoDates = getView().getListOfDemoNotesTitles();
            notes.add(new Note(demoDates[0], DateTime.now().plusDays(2), NoteColors.green));
            notes.add(new Note(demoDates[1], DateTime.now().plusYears(1).plusDays(2), NoteColors.indigo));
            notes.add(new Note(demoDates[2], DateTime.now().plusYears(1).plusDays(1), NoteColors.orange));
            notes.add(new Note(demoDates[3], DateTime.now().plusDays(30), NoteColors.brown));
            notes.add(new Note(demoDates[4], DateTime.now().plusDays(30), NoteColors.gray));
            notes.add(new Note(demoDates[5], DateTime.now().plusDays(20), NoteColors.blue));
            notes.add(new Note(demoDates[6], DateTime.now().plusDays(7), NoteColors.orange));
            notes.add(new Note(demoDates[7], DateTime.now().plusYears(1), NoteColors.blue));
        }
    }

}
