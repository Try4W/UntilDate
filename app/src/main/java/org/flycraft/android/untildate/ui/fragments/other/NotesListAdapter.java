package org.flycraft.android.untildate.ui.fragments.other;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.flycraft.android.untildate.R;
import org.flycraft.android.untildate.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private static final String TAG = "NotesListAdapter";

    private List<Note> notes;
    private OnItemClickListener itemClickListener;

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

    public void setNotes(List<Note> notes) {
        this.notes = new ArrayList<>(notes);
        notifyDataSetChanged();
    }

    public void appendNote(Note note) {
        insertNote(0, note);
    }

    public void insertNote(int position, Note note) {
        notes.add(position, note);
        notifyItemInserted(position);
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
