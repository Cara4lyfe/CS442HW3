package com.example.chandler.cs442hw3;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AsyncLoad extends AsyncTask<String, Integer, String> {

    private static final String TAG = "AsyncLoad";
    private MainActivity ma;
    private String fileName;
    private String encoding;
    StringBuilder jsonString;

    public AsyncLoad(MainActivity ma) {
        this.ma = ma;
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: ");
        try {
            if (strings[0] != null)
                fileName = strings[0];
            if (strings[1] != null)
                encoding = strings[1];

            InputStream is = ma.getApplicationContext().openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));

            jsonString = new StringBuilder();
            String tmp;
            while ((tmp = br.readLine()) != null){
                jsonString.append(tmp);
            }
            br.close();
        } catch (FileNotFoundException e) {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString.toString();
    }
}

