package com.mohamed.simplenote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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

        this.noteTitleView = (TextView) this.findViewById(R.id.noteTitleView);
        // make the TextView Scrolling
        this.noteTitleView.setMovementMethod(new ScrollingMovementMethod());

        this.noteContentView = (TextView) this.findViewById(R.id.noteContentView);
        // make the TextView Scrolling
        this.noteContentView.setMovementMethod(new ScrollingMovementMethod());

        Intent intent = this.getIntent();
        this.note = (Note) intent.getSerializableExtra("note");
        this.noteTitleView.setText(note.getNoteTitle());
        this.noteContentView.setText(note.getNoteContent());
    }

}
