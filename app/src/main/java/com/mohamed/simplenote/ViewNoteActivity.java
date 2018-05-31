package com.mohamed.simplenote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewNoteActivity extends AppCompatActivity {

    private Note note;

    private TextView noteTitleView;
    private TextView noteContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitleView = (TextView) findViewById(R.id.noteTitleView);
        noteContentView = (TextView) findViewById(R.id.noteContentView);

        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        noteTitleView.setText(note.getNoteTitle());
        noteContentView.setText(note.getNoteContent());
    }

}
