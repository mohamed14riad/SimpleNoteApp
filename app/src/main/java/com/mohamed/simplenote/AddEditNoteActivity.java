package com.mohamed.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {

    private Note note;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;

    private EditText noteTitle;
    private EditText noteContent;

    private boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.noteTitle = (EditText)this.findViewById(R.id.noteTitle);
        this.noteContent = (EditText)this.findViewById(R.id.noteContent);

        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        if(note == null) {
            this.mode = MODE_CREATE;
        } else {
            this.mode = MODE_EDIT;
            this.noteTitle.setText(note.getNoteTitle());
            this.noteContent.setText(note.getNoteContent());
        }

    }

    // User Click on the Save button.
    public void saveButtonClicked(View view)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        String title = this.noteTitle.getText().toString();
        String content = this.noteContent.getText().toString();

        if(title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter title & content", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mode==MODE_CREATE ) {
            this.note= new Note(title,content);
            db.addNote(note);
        } else {
            this.note.setNoteTitle(title);
            this.note.setNoteContent(content);
            db.updateNote(note);
        }

        this.needRefresh = true;

        // Back to MainActivity.
        this.onBackPressed();
    }

    // User Click on the Cancel button.
    public void cancelButtonClicked(View view)  {
        // Do nothing, back MainActivity.
        this.onBackPressed();
    }

    // When completed this Activity,
    // Send feedback to the Activity called it.
    @Override
    public void finish() {

        // Create Intent
        Intent data = new Intent();

        // Request MainActivity refresh its ListView (or not).
        data.putExtra("needRefresh", needRefresh);

        // Set Result
        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }

}
