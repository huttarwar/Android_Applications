package com.example.cs442huttarwar.multinotes;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Async_Task extends AsyncTask<String, Integer, String> {

    private  MainActivity mainActivity;
    private int ctr;

    private final String dataURL = "";

    public Async_Task(MainActivity mainact){
        mainActivity = mainact;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Notes are loading.....", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(String s) {
        ArrayList<Note> note = new ArrayList<>();
        mainActivity.dataUpdate(note);
    }


    @Override
    protected String doInBackground(String... strings) {
        String object = null;
        List<Note> noteList;
        try{
            noteList = getAllNotes(mainActivity);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return object;
    }

    private List<Note> getAllNotes(MainActivity mainActivity) throws IOException {
        InputStream inputStream = mainActivity.getApplicationContext().openFileInput("save_note.json");
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try{
            return get_Message_Array(jsonReader);
        }
        finally {
            jsonReader.close();
        }
    }

    private List<Note> get_Message_Array(JsonReader jsonReader) throws IOException {
        List<Note> message = new ArrayList<Note>();

        jsonReader.beginArray();
        while(jsonReader.hasNext()){
            message.add(get_message(jsonReader));
        }
        jsonReader.endArray();
        return message;
    }

    private Note get_message(JsonReader jsonReader) throws IOException {
        Note note1 = new Note(999);
        jsonReader.beginObject();
        while(jsonReader.hasNext()){
            String key = jsonReader.nextName();
            if(key.equals("NOTENUMBER")){
                note1.setNoteNumber(Integer.parseInt(jsonReader.nextString()));
            }
            else if(key.equals("TITLE")){
                note1.setTitle(jsonReader.nextString());
            }
            else if(key.equals("DESCRIPTION")){
                note1.setDescription(jsonReader.nextString());
            }
            else if(key.equals("DATETIME")){
                note1.setDateTime(jsonReader.nextString());
            }
            else{
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        if(note1.getNoteNumber()==999){
            return null;
        }
        return note1;
    }

    public Note file_load(MainActivity main){
        Note note1 = new Note();
        try{
            InputStream inputStream = mainActivity.getApplicationContext().openFileInput("save_note_temporary.json");
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));

            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                String key = jsonReader.nextName();
                if(key.equals("NOTENUMBER")){
                    note1.setNoteNumber(Integer.parseInt(jsonReader.nextString()));
                }
                else if(key.equals("TITLE")){
                    note1.setTitle(jsonReader.nextString());
                }
                else if(key.equals("DESCRIPTION")){
                    note1.setDescription(jsonReader.nextString());
                }
                else if(key.equals("DATETIME")){
                    note1.setDateTime(jsonReader.nextString());
                }
                else{
                    jsonReader.skipValue();
                }
            }
            jsonReader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(mainActivity, "No Notes Found", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return note1;
    }
}
