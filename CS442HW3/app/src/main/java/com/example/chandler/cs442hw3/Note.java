package com.example.chandler.cs442hw3;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable{

    private String title;
    private String content;
    public static SimpleDateFormat formatter = new SimpleDateFormat("E MMM d" + ", " + "h:mm a");
    private String time;

    public Note() {
        this.title = "";
        this.content = "";
        this.time= formatter.format(new Date());
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public String getTime() { return this.time;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return title+content+time;
    }
}
