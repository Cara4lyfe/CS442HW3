package com.example.chandler.cs442hw3;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteView> {

    private static final String TAG = "NoteAdapter";
    private List<Note> noteList;
    private MainActivity mainAct;

    public NoteAdapter(List<Note> empList, MainActivity ma) {
        this.noteList = empList;
        mainAct = ma;
    }

    @Override
    public NoteView onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_list_row, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new NoteView(itemView);
    }

    @Override
    public void onBindViewHolder(NoteView holder, int position) {
        Log.d(TAG, "LOAD VIEW");
        Note note = noteList.get(position);
        holder.title.setText(note.getTitle());
        if (note.getContent().length() > 80){
            holder.content.setText(note.getContent().substring(0, 79) + "...");
        }
        else{
            holder.content.setText(note.getContent());
        }
        holder.time.setText(note.getTime());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

}
