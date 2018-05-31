package com.mohamed.simplenote;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.ListClickListener {

    private RecyclerView notesRecyclerView = null;
    private List<Note> notes = null;
    private NotesAdapter adapter = null;

    private DatabaseHelper databaseHelper = null;

    // IDs for ContextMenu items
    private static final int MENU_ITEM_OPEN = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_DELETE = 333;

    private static final int ADD_EDIT_REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesRecyclerView = (RecyclerView) findViewById(R.id.notesRecyclerView);
        databaseHelper = new DatabaseHelper(this);

        notes = databaseHelper.getAllNotes();
        adapter = new NotesAdapter(this, notes, this);

        notesRecyclerView.setAdapter(adapter);

        // Register the RecyclerView for Context menu
        registerForContextMenu(notesRecyclerView);
    }

    @Override
    public void onItemClick(int position) {
        Note selectedNote = notes.get(position);
        Intent intent = new Intent(this, ViewNoteActivity.class);
        // Put Serializable Extra
        intent.putExtra("note", selectedNote);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addNote:
                Intent intent = new Intent(this, AddEditNoteActivity.class);
                // Start AddEditNoteActivity, (with feedback).
                startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        try {
            position = NotesAdapter.getPosition();
        } catch (Exception e) {
            return super.onContextItemSelected(item);
        }

        if (position >= 0) {
            final Note selectedNote = notes.get(position);

            if (item.getItemId() == MENU_ITEM_OPEN) {
                Intent intent = new Intent(this, ViewNoteActivity.class);
                // Put Serializable Extra
                intent.putExtra("note", selectedNote);
                startActivity(intent);
            } else if (item.getItemId() == MENU_ITEM_EDIT) {
                Intent intent = new Intent(this, AddEditNoteActivity.class);
                // Put Serializable Extra
                intent.putExtra("note", selectedNote);
                // Start AddEditNoteActivity, (with feedback).
                startActivityForResult(intent, ADD_EDIT_REQUEST_CODE);
            } else if (item.getItemId() == MENU_ITEM_DELETE) {
                // Ask before deleting.
                new AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to delete \"" + selectedNote.getNoteTitle() + "\" ?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deleteNote(selectedNote);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        } else {
            return false;
        }

        return super.onContextItemSelected(item);
    }

    // Delete a record
    private void deleteNote(Note note) {
        databaseHelper.deleteNote(note);
        notes.clear();
        notes = databaseHelper.getAllNotes();
        // Notify the data change (To refresh the RecyclerView).
        adapter.addNewList(notes);
    }

    // When AddEditNoteActivity completed, it sends feedback.
    // "If you start it using startActivityForResult()"
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == ADD_EDIT_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", false);
            // Refresh RecyclerView
            if (needRefresh) {
                notes.clear();
                notes = databaseHelper.getAllNotes();
                // Notify the data change (To refresh the RecyclerView).
                adapter.addNewList(notes);
            }
        }
    }
}
