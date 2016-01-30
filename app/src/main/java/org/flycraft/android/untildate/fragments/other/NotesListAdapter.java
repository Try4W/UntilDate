package org.flycraft.android.untildate.fragments.other;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.data.Note;
import org.flycraft.android.untildate.fragments.NotesListFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private static final String TAG = "NotesListAdapter";

    private List<Note> notes;
    private OnItemClickListener itemClickListener;

    public NotesListAdapter(List<Note> notes) {
        this.notes = new ArrayList<>(notes);
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(R.layout.card_note, parent, false);

        final NoteViewHolder holder = new NoteViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(view, holder);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.setData(note);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void appendNote(Note note) {
        addNote(0, note);
    }

    public void addNote(int position, Note note) {
        boolean isFirstItem = notes.isEmpty();
        notes.add(position, note);
        if(!isFirstItem) {
            notifyItemInserted(0);
        }
    }

    public List<Note> getNotes() {
        return notes;
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view , RecyclerView.ViewHolder holder);
    }

}
