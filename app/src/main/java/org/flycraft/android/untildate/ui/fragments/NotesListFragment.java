package org.flycraft.android.untildate.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby.mvp.MvpFragment;

import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.ui.activities.addnote.AddNewNoteActivity;
import org.flycraft.android.untildate.ui.activities.editnote.EditNoteActivity;
import org.flycraft.android.untildate.ui.activities.main.MainActivity;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.ui.fragments.other.ItemTouchHelperAdapter;
import org.flycraft.android.untildate.ui.fragments.other.NotesListAdapter;
import org.flycraft.android.untildate.ui.fragments.other.SimpleItemTouchHelperCallback;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NotesListFragment
        extends MvpFragment<NotesListFragmentView, NotesListFragmentPresenter>
        implements NotesListFragmentView {

    private static final String TAG = "NotesListFragment";

    public static final int ADD_NOTE_REQUEST = 0;
    public static final int EDIT_NOTE_REQUEST = 1;

    private MainActivity mainActivity;

    @Bind(R.id.notes_recycler_view) RecyclerView notesRv;
    @Bind(R.id.gap_text_view) TextView gapTextView;

    private RecyclerView.LayoutManager notesListLayoutManager;
    private NotesListAdapter notesRvAdapter;
    private ItemTouchHelper itemTouchHelper;
    private SimpleItemTouchHelperCallback itemTouchHelperCallback;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainActivity = (MainActivity) getActivity();

        ButterKnife.bind(this, view);

        if(MainActivity.isDebugMode()) {
            presenter.tryToAddDemoNotesSet();
        }

        setupNotesList();
        setupTouchHelper();

        fab = mainActivity.getFab();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteActivity();
            }
        });

        presenter.initialize();
    }

    @NonNull
    @Override
    public NotesListFragmentPresenter createPresenter() {
        return new NotesListFragmentPresenter();
    }

    @Override
    public void setGapState(boolean state) {
        if(state) {
            notesRv.setVisibility(View.GONE);
            gapTextView.setVisibility(View.VISIBLE);
        } else {
            notesRv.setVisibility(View.VISIBLE);
            gapTextView.setVisibility(View.GONE);
        }
    }

    private void setupNotesList() {
        notesRv.setHasFixedSize(true);

        notesListLayoutManager = new LinearLayoutManager(getActivity());
        notesRv.setLayoutManager(notesListLayoutManager);

        notesRvAdapter = new NotesListAdapter();
        notesRv.setAdapter(notesRvAdapter);
        notesRvAdapter.setOnItemClickListener(new NotesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder) {
                presenter.noteClick(holder.getAdapterPosition());
            }
        });
    }

    private void setupTouchHelper() {
        ItemTouchHelperAdapter adapter = new ItemTouchHelperAdapter() {
            @Override
            public void onItemDismiss(int position) {
                presenter.dismissNote(position);
            }

            @Override
            public void onDragEnd(RecyclerView.ViewHolder holder) {
                // do nothing
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                presenter.moveNote(fromPosition, toPosition);
            }
        };

        itemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(notesRv);
    }

    @Override
    public void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(
                mainActivity.getCoordinatorLayout(),
                R.string.date_removed,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.undoRemoveAction();
                    }
                });
        snackbar.show();
    }

    @Override
    public void openAddNoteActivity() {
        Intent intent = new Intent(getContext(), AddNewNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
    }

    @Override
    public void openEditNoteActivity(int noteIndexToEdit) {
        Intent intent = new Intent(getContext(), EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.NOTE_POS_KEY, noteIndexToEdit);
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
    }

    @Override
    public void appendNote(Note note) {
        notesRvAdapter.appendNote(note);
        notesRv.scrollToPosition(0);
    }

    @Override
    public void insertNote(int position, Note note) {
        Log.d(TAG, "insertNote: " + position + "/" + note);
        notesRvAdapter.insertNote(position, note);
        notesRv.scrollToPosition(position);
    }

    @Override
    public void updateNote(int position) {
        notesRvAdapter.notifyItemChanged(position);
    }

    @Override
    public void moveNote(int fromPosition, int toPosition) {
        List<Note> rvData = notesRvAdapter.getNotes();
        Collections.swap(rvData, fromPosition, toPosition);
        notesRvAdapter.notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void removeNote(int position) {
        List<Note> rvData = notesRvAdapter.getNotes();
        rvData.remove(position);
        notesRvAdapter.notifyItemRemoved(position);
    }

    @Override
    public void setNotesListData(List<Note> notes) {
        notesRvAdapter.setNotes(notes);
    }

    @Override
    public String[] getListOfDemoNotesTitles() {
        return getResources().getStringArray(R.array.demo_notes);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_NOTE_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    presenter.newNoteAdded();
                }
                break;
            case EDIT_NOTE_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if(extras == null) {
                        throw new NullPointerException("No returned data from EditNoteActivity");
                    }
                    int noteIndex = extras.getInt(EditNoteActivity.NOTE_POS_KEY);
                    presenter.noteEdited(noteIndex);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
