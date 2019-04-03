package com.example.cs442huttarwar.multinotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.example.cs442huttarwar.multinotes.R.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    private RecyclerView recyclerView;
    private List<Note> Notelist = new ArrayList<>();
    private NoteAdapter madapter;
    private int sizeofnotes = 0;
    EditNoteActivity editNoteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        recyclerView = findViewById(R.id.recycler);

        madapter = new NoteAdapter(Notelist, this);
        recyclerView.setAdapter(madapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Async_Task(this).execute();
        Notelist.clear();
        try{
            Notelist.addAll(getNotes(MainActivity.this));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Note> getNotes(MainActivity mainActivity) throws IOException {
        InputStream inputStream = mainActivity.getApplicationContext().openFileInput("save_note.json");
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        try{
            return get_Message_Array(reader);
        }
        finally {
            reader.close();
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
        Note note1 = new Note(1000);
        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String key = jsonReader.nextName();
            if (key.equals("NOTENUMBER")) {
                note1.setNoteNumber(Integer.parseInt(jsonReader.nextString()));
            } else if (key.equals("TITLE")) {
                note1.setTitle(jsonReader.nextString());
            } else if (key.equals("DESCRIPTION")) {
                note1.setDescription(jsonReader.nextString());
            } else if (key.equals("DATETIME")) {
                note1.setDateTime(jsonReader.nextString());
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        if (note1.getNoteNumber() == 1000) {
            return null;
        }
        return note1;
    }

    public Note file_load(MainActivity mainActivity){
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



    @Override
    protected void onStart() {
        super.onStart();

        madapter = new NoteAdapter(Notelist, this);
        recyclerView.setAdapter(madapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Note temp = file_load(this);
        if(temp.getNoteNumber() != 999){
            int jhenda = 0;
            for(int i = 0; i < Notelist.size(); i++){
                if(temp.getNoteNumber() == Notelist.get(i).getNoteNumber()){
                Notelist.get(i).setTitle(temp.getTitle());
                Notelist.get(i).setDescription(temp.getDescription());
                Notelist.get(i).setDateTime(temp.getDateTime());
                jhenda = 1;
                break;
            }
            }
            if(jhenda == 0){
                if(temp.getNoteNumber() !=0 ){
                    Notelist.add(0,temp);
                }
            }
            setNotes(Notelist, this);
        }
        recyclerView.setAdapter(madapter);
    }

    private void setNotes(List<Note> notelist, MainActivity mainActivity) {

        try{
            FileOutputStream fileOutputStream = mainActivity.getApplicationContext().openFileOutput("save_note.json", Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
            writer.setIndent("  ");
            set_Entry(writer, notelist);
            writer.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void set_Entry(JsonWriter writer, List<Note> notelist) throws IOException {

        writer.beginArray();
        for( Note note: notelist){
            set_Entry_json(writer, note);
        }
        writer.endArray();
    }

    private void set_Entry_json(JsonWriter writer, Note note) throws IOException{
       writer.beginObject();
        writer.name("NOTENUMBER").value(note.getNoteNumber());
        writer.name("DATETIME").value(note.getDateTime());
        writer.name("TITLE").value(note.getTitle());
        writer.name("DESCRIPTION").value(note.getDescription());
        writer.endObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
        case R.id.about:
            Intent intent_aboutus = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent_aboutus);
            return true;

        case R.id.addNote:
            sizeofnotes++;
            Note addNote = new Note(sizeofnotes);
            Intent intent = new Intent(MainActivity.this,EditNoteActivity.class);
            intent.putExtra(Note.class.getName(), addNote);
            intent.putExtra("size", sizeofnotes);
            startActivity(intent);
            return true;

        default:
            return super.onOptionsItemSelected(item);
    }
    }

    @Override
    public void onClick(View view) {
        int note_number = recyclerView.getChildLayoutPosition(view);
        Note note1 = Notelist.get(note_number);
        Notelist.remove(note_number);
        Notelist.add(0, note1);
        Intent intent_click = new Intent(MainActivity.this, EditNoteActivity.class);
        intent_click.putExtra(Note.class.getName(), note1);
        startActivity(intent_click);

    }

    @Override
    public boolean onLongClick(final View view) {
        AlertDialog.Builder alert_delete= new AlertDialog.Builder(this).setTitle("Delete").setMessage("Do you want to delete the note?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int note_number = recyclerView.getChildLayoutPosition(view);
                Toast.makeText(view.getContext(), "Note Deleted" , Toast.LENGTH_SHORT).show();
                Notelist.remove(note_number);
                setNotes(Notelist, MainActivity.this);
                recyclerView.setAdapter(madapter);
            }
        }).setNegativeButton("No",null).setCancelable(false);
        alert_delete.show();
        return true;
    }

    public void dataUpdate(ArrayList<Note> note) {
        recyclerView.setAdapter(madapter);
    }
}

