package com.example.cs442huttarwar.multinotes;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Serializable {

    private String title;
    private String description;
    private String dateTime;
    private int noteNumber;

    public Note(int noteNumber)
    {
        this.noteNumber = noteNumber;
        String currentDateandTime = new SimpleDateFormat("EEE MMM d, h:mm a").format(new Date());
        this.description = "";
        this.dateTime = "";
        this.title = "";
    }

    public Note(){

    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getTitle() {
        return title;
    }

    public int getNoteNumber() {
        return noteNumber;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setNoteNumber(int noteNumber){
        this.noteNumber = noteNumber;
    }

    public String toString(){
        return noteNumber+dateTime+":"+title+description;
    }
}
