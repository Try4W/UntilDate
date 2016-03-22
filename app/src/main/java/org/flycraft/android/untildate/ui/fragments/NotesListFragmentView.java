package org.flycraft.android.untildate.ui.fragments;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.flycraft.android.untildate.data.Note;

import java.util.List;

public interface NotesListFragmentView extends MvpView {

    void setNotesListData(List<Note> notes);
    void setGapState(boolean gapState);
    void openAddNoteActivity();
    void openEditNoteActivity(int notePosition);
    void appendNote(Note note);
    void insertNote(int position, Note note);
    void updateNote(int position);
    void moveNote(int fromPosition, int toPosition);
    void removeNote(int position);
    void showUndoSnackbar();
    String[] getListOfDemoNotesTitles();

}
