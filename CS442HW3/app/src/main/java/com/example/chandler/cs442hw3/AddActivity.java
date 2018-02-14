package com.example.chandler.cs442hw3;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText title;
    private EditText content;
    String oldTitle;
    String oldContent;

    private static final String TAG = "AddActivity";
    public static SimpleDateFormat formatter = new SimpleDateFormat("E MMM d" + ", " + "h:mm a");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        Intent intent = getIntent();
        if (intent.hasExtra("Add")) {
            Note note = (Note) intent.getSerializableExtra("Add");
            oldTitle = note.getTitle().toString();
            oldContent = note.getContent().toString();
            Log.d(TAG, "GotNote:" + oldTitle + oldContent);
            title.setText(oldTitle);
            content.setText(oldContent);
        }
        else{
            oldTitle="";
            oldContent="";
            title.setText(oldTitle);
            content.setText(oldContent);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        switch (item.getItemId()) {
            case R.id.save:
                if (title.getText().toString().equals("")){
                    Toast.makeText(this, "Not Saving (No Title)", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "SAME");
                    setResult(RESULT_CANCELED);
                }
                else if(oldTitle.equals(title.getText().toString())&& oldContent.equals(content.getText().toString())){
                    Toast.makeText(this, "Not Saving (No Changes)", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "SAME");
                    setResult(RESULT_CANCELED);
                }
                else{
                    Toast.makeText(this, "Saving a New Note", Toast.LENGTH_SHORT).show();
                    Intent data = new Intent();
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setContent(content.getText().toString());
                    note.setTime(formatter.format(new Date()));
                    data.putExtra("Add", note);
                    setResult(RESULT_OK, data);
                }
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // Pressing the back arrow closes the current activity, returning us to the original activity
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                title = findViewById(R.id.title);
                content = findViewById(R.id.content);
                if (title.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Not Saving (No Title)", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                }
                else if(oldTitle.equals(title.getText().toString())&& oldContent.equals(content.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Not Saving (No Changes)", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_CANCELED);
                }
                else{
                    Intent data = new Intent();
                    Note note = new Note();
                    note.setTitle(title.getText().toString());
                    note.setContent(content.getText().toString());
                    note.setTime(formatter.format(new Date()));
                    data.putExtra("Add", note);
                    setResult(RESULT_OK, data);
                }
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        builder.setMessage("Do you wish to save?");
        builder.setTitle("Save and Exit?");

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
