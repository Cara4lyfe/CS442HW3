package com.example.chandler.cs442hw3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener {

        // The above lines are important - them make this class a listener
        // for click and long click events in the ViewHolders (in the recycler
        private static final int B_REQUEST_CODE = 1;
        private static final int A_REQUEST_CODE = 0;
        private List<Note> noteList = new ArrayList<>();  // Main content is here

        private RecyclerView recyclerView; // Layout's recyclerview

        private NoteAdapter mAdapter; // Data to recyclerview adapter

        private static final String TAG = "MainActivity";

        private Note note;

        int index;

    @Override
    public void onClick(View v) {  // click listener called by ViewHolder clicks
        Toast.makeText(v.getContext(), "Editing Note" , Toast.LENGTH_SHORT).show();
        index = recyclerView.getChildLayoutPosition(v);
        Intent intentAdd = new Intent(MainActivity.this, AddActivity.class);
        Log.d(TAG, "Note:" + noteList.get(index));
        intentAdd.putExtra("Add", noteList.get(index));
        startActivityForResult(intentAdd, B_REQUEST_CODE);
    }

    @Override
    public boolean onLongClick(View v) {  // long click listener called by ViewHolder long clicks
        index = recyclerView.getChildLayoutPosition(v);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteList.remove(index);
                mAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setMessage("Are you sure to delete this?");
        builder.setTitle("DELETE THIS NOTE");

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        mAdapter = new NoteAdapter(noteList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*List<Note> loadnoteList = loadFile();  // Load the JSON containing the product data - if it exists
        if (noteList != null) { // null means no file was loaded
            for (int i = 0; i < loadnoteList.size(); i++) {
                noteList.get(i).setTitle((loadnoteList.get(i).getTitle()));
                noteList.get(i).setContent((loadnoteList.get(i).getContent()));
                noteList.get(i).setTime((loadnoteList.get(i).getTime()));
            }

        }*/
        /*for (int i = 0; i < 20; i++) {
            Note note = new Note();
            note.setTitle(Integer.toString(i));
            note.setContent(Integer.toString(i+1));
            note.setTime(formatter.format(new Date()));
            noteList.add(note);
            Log.d(TAG, "ADD" + note.getTitle()+note.getContent()+note.getTime()+noteList);
        }*/
        AsyncLoad asyncLoad = new AsyncLoad(this);
        asyncLoad.execute("note.json", "UTF-8");
    }

    @Override
    protected void onResume() {
        List<Note> loadnoteList = loadFile();  // Load the JSON containing the product data - if it exists
        List<Note> newList = new ArrayList<>();
        Log.d(TAG,"list size:"+loadnoteList.size());
        if (loadnoteList.size()>0) {
            Log.d(TAG,"list:"+loadnoteList);
            for (int i = 0; i < loadnoteList.size(); i++) {
                Note newNote = new Note();
                Log.d(TAG,"load:"+i+": "+loadnoteList.get(i));
                newNote.setTitle((loadnoteList.get(i).getTitle()));
                newNote.setContent((loadnoteList.get(i).getContent()));
                newNote.setTime((loadnoteList.get(i).getTime()));
                Log.d(TAG,"New Note:"+newNote);
                newList.add(newNote);
                Log.d(TAG,"New List:"+newList);
            }
        }
        Log.d(TAG,"New List:"+newList);
        noteList = newList;
        mAdapter = new NoteAdapter(noteList, this);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.notifyDataSetChanged();
        super.onResume();
    }
    private List<Note> loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        List<Note> loadnoteList = new ArrayList<>();
        try {
            InputStream is = getApplicationContext().openFileInput("note.json");
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));

            reader.beginObject();
            while (reader.hasNext()) {
                note = new Note();
                for (int i = 0; i<3; i++){
                    String name = reader.nextName();
                    Log.d(TAG,"name:"+name);
                    if (name.equals("content")) {
                        note.setContent(reader.nextString());
                    }
                    else if (name.equals("title")) {
                        note.setTitle(reader.nextString());
                    }
                    else if (name.equals("time")) {
                        note.setTime(reader.nextString());
                    }
                    else {
                        reader.skipValue();
                    }
                }
                Log.d(TAG,"note:"+note);
                loadnoteList.add(note);
                Log.d(TAG,"load list:"+loadnoteList);
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Log.d(TAG,"no file");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loadnoteList;
    }
    @Override
    protected void onPause() {
        saveProduct();
        super.onPause();
    }
    protected void onStop() {
        saveProduct();
        super.onStop();
    }
    private void saveProduct() {

        Log.d(TAG, "saveProduct: Saving JSON File");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput("note.json", Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos,"UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            for (int i = 0; i < noteList.size(); i++) {
                writer.name("title").value(noteList.get(i).getTitle());
                writer.name("content").value(noteList.get(i).getContent());
                writer.name("date").value(noteList.get(i).getTime());
            }
            writer.endObject();
            writer.close();
            /// You do not need to do the below - it's just
            /// a way to see the JSON that is created.
            ///
            StringWriter sw = new StringWriter();
            writer = new JsonWriter(sw);
            writer.setIndent("  ");
            writer.beginObject();
            for (int i = 0; i < noteList.size(); i++) {
                writer.name("title").value(noteList.get(i).getTitle());
                writer.name("content").value(noteList.get(i).getContent());
                writer.name("date").value(noteList.get(i).getTime());
            }
            writer.endObject();
            writer.close();
            Log.d(TAG, "saveProduct: JSON:\n" + sw.toString());
            ///
            ///

            /*Toast.makeText(this, "File Saved", Toast.LENGTH_SHORT).show();*/
        } catch (Exception e) {
            e.getStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.about:
                Toast.makeText(this, "Jumping to About Page", Toast.LENGTH_SHORT).show();
                Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
                String applicationName = getResources().getString(R.string.app_name);
                intentAbout.putExtra(Intent.EXTRA_TEXT, applicationName);
                startActivity(intentAbout);
                return true;
            case R.id.add:
                Toast.makeText(this, "Creating a New Note", Toast.LENGTH_SHORT).show();
                Intent intentAdd = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(intentAdd, A_REQUEST_CODE);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == B_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Note note = (Note) data.getSerializableExtra("Add");
                if (noteList.size()>0){
                    noteList.remove(index);
                }
                noteList.add(0, note);
                mAdapter.notifyDataSetChanged();
                saveProduct();
                Log.d(TAG, "Changed Note");
            }
            else if(resultCode == RESULT_CANCELED){
                Log.d(TAG, "Not saved");
            }
            else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else if (requestCode == A_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Note note = (Note) data.getSerializableExtra("Add");
                noteList.add(0, note);
                mAdapter.notifyDataSetChanged();
                saveProduct();
                Log.d(TAG, "Changed Note");
            }
            else if(resultCode == RESULT_CANCELED){
                Log.d(TAG, "Not saved");
            }
            else {
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        }
        else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
        }

    }
}
