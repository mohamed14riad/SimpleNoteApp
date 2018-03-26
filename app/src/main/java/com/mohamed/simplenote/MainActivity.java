package com.mohamed.simplenote;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView notesListView;

    // IDs for Menu items
    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_DELETE = 333;

    private static final int MY_REQUEST_CODE = 1000;

    private final List<Note> notesList = new ArrayList<Note>();
    private ArrayAdapter<Note> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get ListView object from xml
        notesListView = (ListView) findViewById(R.id.notesListView);
        notesListView.setOnItemClickListener(this);

        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);

        List<Note> list = db.getAllNotes();
        this.notesList.addAll(list);

        // Define a new Adapter
        // 1 - Context
        // 2 - Layout for the row
        // 3 - the List of data
        this.listViewAdapter = new ArrayAdapter<Note>(MainActivity.this, android.R.layout.select_dialog_item, this.notesList);

        // Assign adapter to ListView
        this.notesListView.setAdapter(this.listViewAdapter);

        // Register the ListView for Context menu
        registerForContextMenu(this.notesListView);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Note selectedNote = (Note) adapterView.getItemAtPosition(position);
        Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
        // Put Serializable Extra
        intent.putExtra("note", selectedNote);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.addNote:
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                // Start AddEditNoteActivity, (with feedback).
                this.startActivityForResult(intent, MY_REQUEST_CODE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Select The Action");

        // groupId, itemId, itemOrder, itemTitle
        menu.add(0, MENU_ITEM_VIEW, 0, "View Note");
        menu.add(0, MENU_ITEM_EDIT, 1, "Edit Note");
        menu.add(0, MENU_ITEM_DELETE, 2, "Delete Note");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        final Note selectedNote = (Note) this.notesListView.getItemAtPosition(info.position);

        if (item.getItemId() == MENU_ITEM_VIEW) {
            Intent intent = new Intent(MainActivity.this, ViewNoteActivity.class);
            // Put Serializable Extra
            intent.putExtra("note", selectedNote);
            this.startActivity(intent);
        } else if (item.getItemId() == MENU_ITEM_EDIT) {
            Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
            // Put Serializable Extra
            intent.putExtra("note", selectedNote);
            // Start AddEditNoteActivity, (with feedback).
            this.startActivityForResult(intent, MY_REQUEST_CODE);
        } else if (item.getItemId() == MENU_ITEM_DELETE) {
            // Ask before deleting.
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete ?\n\n" + selectedNote.getNoteTitle())
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            deleteNote(selectedNote);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            return false;
        }

        return true;
    }

    // Delete a record
    private void deleteNote(Note note) {
        MyDatabaseHelper db = new MyDatabaseHelper(this);
        db.deleteNote(note);
        this.notesList.remove(note);
        // Refresh ListView.
        this.listViewAdapter.notifyDataSetChanged();
    }

    // When AddEditNoteActivity completed, it sends feedback.
    // (If you start it using startActivityForResult ())
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE) {
            boolean needRefresh = data.getBooleanExtra("needRefresh", true);
            // Refresh ListView
            if (needRefresh) {
                this.notesList.clear();
                MyDatabaseHelper db = new MyDatabaseHelper(this);
                List<Note> list = db.getAllNotes();
                this.notesList.addAll(list);
                // Notify the data change (To refresh the ListView).
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }

}
