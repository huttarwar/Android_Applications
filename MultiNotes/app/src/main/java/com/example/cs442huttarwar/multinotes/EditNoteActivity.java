package com.example.cs442huttarwar.multinotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity implements Serializable {

    private static final String TAG = "";
    private EditText editTitle;
    private EditText editNote;
    Note note;
    int size;
    private String changes_Title;
    private String hasChanges;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_text);
        editTitle = findViewById(R.id.edit_title);
        editNote = findViewById(R.id.edit_description);

        editNote.setMovementMethod(new ScrollingMovementMethod());

        editNote.setTextIsSelectable(true);
        editTitle.setTextIsSelectable(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent= getIntent();
        if (intent.hasExtra(Note.class.getName())){
            note = (Note) intent.getSerializableExtra(Note.class.getName());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onStart();
        if (note != null){
            hasChanges = note.getDescription();
            editTitle.setText(note.getTitle());
            editTitle.setSelection(note.getTitle().length());
            editNote.setText(note.getDescription());
            changes_Title = note.getDescription();
        }
    }

    @Override
    protected void onPause() {
        note.setTitle(editTitle.getText().toString());
        note.setDescription(editNote.getText().toString());
        if(note.getTitle().equals("")){
            Toast.makeText(getApplicationContext(), "Untitled Activity is not saved", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
            {
             if(!hasChanges.equals(editNote.getText().toString())){
                 saveNotes(note,this);
             }
            }
        super.onPause();
    }

    private void saveNotes(Note note, EditNoteActivity editNoteActivity) {

        try{
            FileOutputStream fileOutputStream = editNoteActivity.getApplicationContext().openFileOutput("save_note_temporary.json",Context.MODE_PRIVATE);
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fileOutputStream, "UTF-8"));
            writer.setIndent("  ");
            writer.beginObject();
            String currentDateandTime = new SimpleDateFormat("EEE MMM d, h:mm a").format(new Date());
            writer.name("NOTENUMBER").value(note.getNoteNumber());
            writer.name("DATETIME").value(currentDateandTime);
            writer.name("TITLE").value(note.getTitle());
            writer.name("DESCRIPTION").value(note.getDescription());
            writer.endObject();
            writer.close();

            StringWriter stringWriter = new StringWriter();
            writer = new JsonWriter(stringWriter);
            writer.setIndent("  ");
            writer.beginObject();
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                size = bundle.getInt("size");
            }
            writer.name("NOTENUMBER").value(size);
            writer.name("DATETIME").value(currentDateandTime);
            writer.name("TITLE").value(note.getTitle());
            writer.name("DESCRIPTION").value(note.getDescription());
            writer.endObject();
            writer.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edittext_bar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                if(!(changes_Title.equals(editTitle.getText().toString()) || hasChanges.equals(editNote.getText().toString()))){
                    note.setTitle(editTitle.getText().toString());
                    note.setDescription(editNote.getText().toString());
                    finish();
                    return true;
                }
                else if(!hasChanges.equals(editNote.getText().toString())){
                        saveNotes(note, this);
                        finish();
                    }
                    note.setTitle(editTitle.getText().toString());
                    note.setTitle(editNote.getText().toString());
                    if(note.getTitle().equals("")){
                        Toast.makeText(getApplicationContext(), "Untitled Activity was not saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        saveNotes(note, this);
                        finish();
                    }
                    return true;

                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Note is not saved\n"+"Do you want to save the note?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditNoteActivity.this.back_pressed();
                if(hasChanges.equals(note.getDescription().toString())){
                    finish();
                }

                note.setTitle(editTitle.getText().toString());
                note.setDescription(editNote.getText().toString());
                if(note.getTitle().equals("")){
                    Toast.makeText(getApplicationContext(), "Untitled Activity is not saved", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    saveNotes(note,EditNoteActivity.this);
                    finish();
                }
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();



    }

    private void back_pressed() {
        super.onBackPressed();
    }
}
