package com.example.chandler.cs442hw3;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NoteView extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView content;
    public TextView time;

    public NoteView(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.title);
        content = (TextView) view.findViewById(R.id.content);
        time = (TextView) view.findViewById(R.id.time);
    }

}