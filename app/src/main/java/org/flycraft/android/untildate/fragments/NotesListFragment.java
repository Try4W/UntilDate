package org.flycraft.android.untildate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.flycraft.android.gapview.GapView;
import org.flycraft.android.untildate.NoteColors;
import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.activities.AddNoteActivity;
import org.flycraft.android.untildate.activities.EditNoteActivity;
import org.flycraft.android.untildate.activities.MainActivity;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.data.NotesStorage;
import org.flycraft.android.untildate.fragments.other.ItemTouchHelperAdapter;
import org.flycraft.android.untildate.fragments.other.NotesListAdapter;
import org.flycraft.android.untildate.fragments.other.SimpleItemTouchHelperCallback;
import org.flycraft.android.untildate.fragments.other.ListTopSpaceDecoration;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.List;

import static org.flycraft.android.untildate.activities.MainActivity.isDebugMode;

public class NotesListFragment extends Fragment {

    private static final String TAG = "MainActivity";

    public static final int ADD_NOTE_REQUEST = 0;
    public static final int EDIT_NOTE_REQUEST = 1;

    private MainActivity mainActivity;

    private GapView gapView;
    private RecyclerView notesRecyclerView;
    private RecyclerView.LayoutManager notesListLayoutManager;
    private NotesListAdapter notesListAdapter;
    private ItemTouchHelper itemTouchHelper;
    private SimpleItemTouchHelperCallback itemTouchHelperCallback;
    private FloatingActionButton fab;
    private MenuItem putDebugDataItem;
    private MenuItem switchItem;

    private NotesStorage storage = NotesStorage.getInstance();

    private boolean editMode;

    private Note lastRemovedNote;
    private int lastRemovedItemPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) getActivity();

        if(MainActivity.isDebugMode() && storage.getNotes().isEmpty()) {
            addDefaultDatesSet();
        }

        gapView = (GapView) view.findViewById(R.id.gap);
        notesRecyclerView = (RecyclerView) view.findViewById(R.id.notes_list);
        setupNotesList();
        setupTouchHelper();
        //fillNotesList();

        fab = mainActivity.getFab();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddNoteActivity();
            }
        });

        updateGapState();
    }

    private void updateGapState() {
        gapView.setShowGap(isNotesListEmpty());
    }

    private void addDefaultDatesSet() {
        List<Note> notes = storage.getNotes();
        notes.add(new Note("Домой <3", DateTime.now().plusDays(2), NoteColors.green));
        notes.add(new Note("Новый год", DateTime.now().plusYears(1).plusDays(2), NoteColors.indigo));
        notes.add(new Note("Новая работа", DateTime.now().plusYears(1).plusDays(1), NoteColors.orange));
        notes.add(new Note("Взять гитару", DateTime.now().plusDays(30), NoteColors.brown));
        notes.add(new Note("Купить кота", DateTime.now().plusDays(30), NoteColors.gray));
        notes.add(new Note("Накидаться", DateTime.now().plusDays(20), NoteColors.blue));
        notes.add(new Note("Вписон", DateTime.now().plusDays(7), NoteColors.orange));
        notes.add(new Note("Первый секс", DateTime.now().plusYears(1), NoteColors.blue));
    }

    private void addDebugListElement() {
        Note note = new Note(
                "Some date",
                DateTime.now().plusHours(2),
                NoteColors.blue
        );
        storage.add(note);
        notesListAdapter.appendNote(note);
    }

    private void switchEditMode() {
        editMode = !editMode;
        setEditMode(editMode);
    }

    private void setEditMode(boolean editMode) {
        if(editMode) {
            switchItem.setIcon(R.drawable.pencil);
            Toast.makeText(getActivity(), getString(R.string.edit_hints), Toast.LENGTH_LONG)
                    .show();
            itemTouchHelperCallback.setDisabled(true);
        } else {
            switchItem.setIcon(R.drawable.eye);
            itemTouchHelperCallback.setDisabled(false);
        }
    }

    private void setupNotesList() {
        notesRecyclerView.setHasFixedSize(true);

        notesRecyclerView.addItemDecoration(new ListTopSpaceDecoration(5));

        notesListLayoutManager = new LinearLayoutManager(getActivity());
        notesRecyclerView.setLayoutManager(notesListLayoutManager);
        //fillNotesList();

        notesListAdapter = new NotesListAdapter(storage.getNotes());
        notesRecyclerView.setAdapter(notesListAdapter);
        notesListAdapter.setOnItemClickListener(new NotesListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder) {
                if (editMode) {
                    openEditNoteActivity(holder.getAdapterPosition());
                }
            }
        });
    }

    private void setupTouchHelper() {
        ItemTouchHelperAdapter adapter = new ItemTouchHelperAdapter() {
            @Override
            public void onItemDismiss(int position) {
                removeListItem(position);
                showUndoSnackbar();
            }

            @Override
            public void onDragEnd(RecyclerView.ViewHolder holder) {
                // do nothing
            }

            @Override
            public void onItemMove(int fromPosition, int toPosition) {
                moveItem(fromPosition, toPosition);
            }
        };

        itemTouchHelperCallback = new SimpleItemTouchHelperCallback(adapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(notesRecyclerView);
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(
                mainActivity.getCoordinatorLayout(),
                R.string.date_removed,
                Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        undoLastRemoveAction();
                    }
                });
        snackbar.show();
    }

    private void removeListItem(int position) {
        final List<Note> globalData = storage.getNotes();
        final List<Note> rvData = notesListAdapter.getNotes();

        lastRemovedItemPosition = position;
        lastRemovedNote = globalData.remove(position);
        rvData.remove(position);
        notesListAdapter.notifyItemRemoved(position);
        updateGapState();
        storage.saveData();
    }

    private void moveItem(int fromPosition, int toPosition) {
        final List<Note> globalData = storage.getNotes();
        final List<Note> rvData = notesListAdapter.getNotes();

        Collections.swap(globalData, fromPosition, toPosition);
        Collections.swap(rvData, fromPosition, toPosition);
        notesListAdapter.notifyItemMoved(fromPosition, toPosition);
        storage.saveData();
    }

    private void undoLastRemoveAction() {
        if(lastRemovedNote == null) {
            throw new RuntimeException("There is no actions to undo");
        }
        storage.getNotes().add(lastRemovedItemPosition, lastRemovedNote);
        notesListAdapter.addNote(lastRemovedItemPosition, lastRemovedNote);
        lastRemovedNote = null;
        updateGapState();
        storage.saveData();
    }

    private void openAddNoteActivity() {
        Intent intent = new Intent(getContext(), AddNoteActivity.class);
        startActivityForResult(intent, ADD_NOTE_REQUEST);
    }

    private void openEditNoteActivity(int noteIndexToEdit) {
        Intent intent = new Intent(getContext(), EditNoteActivity.class);
        intent.putExtra(EditNoteActivity.NOTE_POS_KEY, noteIndexToEdit);
        startActivityForResult(intent, EDIT_NOTE_REQUEST);
    }

    // NOTE: I must imminently call notify*** after changing data set

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_NOTE_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    notesListAdapter.appendNote(storage.getNotes().get(0)); // this note was added in AddNoteActivity
                    updateGapState();
                    notesRecyclerView.scrollToPosition(0);
                }
                break;
            case EDIT_NOTE_REQUEST:
                if(resultCode == Activity.RESULT_OK) {
                    Bundle extras = data.getExtras();
                    if(extras == null) {
                        throw new NullPointerException("No returned data from EditNoteActivity");
                    }
                    int noteIndex = extras.getInt(EditNoteActivity.NOTE_POS_KEY);
                    notesListAdapter.notifyItemChanged(noteIndex);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isNotesListEmpty() {
        return notesRecyclerView.getAdapter().getItemCount() == 0;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(isDebugMode()) {
            putDebugDataItem = menu.add(0, 1338, 1, "ADI");
            putDebugDataItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        switchItem = menu.add(0, 1337, 0, R.string.switch_mode).setIcon(R.drawable.eye);
        switchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(switchItem.getItemId() == item.getItemId()) {
            switchEditMode();
            return true;
        }
        if(isDebugMode() && putDebugDataItem.getItemId() == item.getItemId()) {
            addDebugListElement();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
