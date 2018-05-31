package com.mohamed.simplenote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEditNoteActivity extends AppCompatActivity {

    private Note note;

    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;

    private EditText noteTitle;
    private EditText noteContent;

    private boolean needRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteContent = (EditText) findViewById(R.id.noteContent);

        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if (note == null) {
            mode = MODE_CREATE;
        } else {
            mode = MODE_EDIT;
            noteTitle.setText(note.getNoteTitle());
            noteContent.setText(note.getNoteContent());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                saveButtonClicked();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // User Click on the Save button.
    public void saveButtonClicked() {
        DatabaseHelper db = new DatabaseHelper(this);

        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please Enter Title & Content", Toast.LENGTH_LONG).show();
            return;
        }

        if (mode == MODE_CREATE) {
            note = new Note(title, content, date);
            db.addNote(note);
        } else {
            note.setNoteTitle(title);
            note.setNoteContent(content);
            note.setNoteDate(date);
            db.updateNote(note);
        }

        needRefresh = true;

        // Create Intent
        Intent data = new Intent();

        // Request MainActivity refresh its RecyclerView (or not).
        data.putExtra("needRefresh", needRefresh);

        // Set Result
        setResult(Activity.RESULT_OK, data);

        // Back to MainActivity.
        finish();
    }
}
