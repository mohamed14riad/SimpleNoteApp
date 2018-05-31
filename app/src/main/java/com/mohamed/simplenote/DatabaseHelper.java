package com.mohamed.simplenote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Note_Database";

    // Table Name
    private static final String TABLE_NOTE = "Note";

    // Table Columns
    private static final String COLUMN_NOTE_ID = "Note_Id";
    private static final String COLUMN_NOTE_TITLE = "Note_Title";
    private static final String COLUMN_NOTE_CONTENT = "Note_Content";
    private static final String COLUMN_NOTE_DATE = "Note_Date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // query.
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NOTE + " (" +
                COLUMN_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTE_TITLE + " TEXT NOT NULL, " +
                COLUMN_NOTE_CONTENT + " TEXT NOT NULL, " +
                COLUMN_NOTE_DATE + " TEXT NOT NULL)";

        // Execute query.
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

        // Create tables again
        onCreate(db);
    }

    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getNoteContent());
        values.put(COLUMN_NOTE_DATE, note.getNoteDate());

        // Inserting Row
        db.insert(TABLE_NOTE, null, values);

        // Closing database connection
        db.close();
    }

    public List<Note> getAllNotes() {
        List<Note> noteList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setNoteId(cursor.getInt(0));
                note.setNoteTitle(cursor.getString(1));
                note.setNoteContent(cursor.getString(2));
                note.setNoteDate(cursor.getString(3));
                // Adding note to list
                noteList.add(note);
            } while (cursor.moveToNext());
        }

        // Closing database connection
        db.close();

        // return note list
        return noteList;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, note.getNoteTitle());
        values.put(COLUMN_NOTE_CONTENT, note.getNoteContent());
        values.put(COLUMN_NOTE_DATE, note.getNoteDate());

        // Updating row
        int updateCount = db.update(TABLE_NOTE, values, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(note.getNoteId())});

        // Closing database connection
        db.close();

        // Return number of updated rows
        return updateCount;
    }

    public void deleteNote(Note note) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Deleting row
        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(note.getNoteId())});
        // Closing database connection
        db.close();
    }

}
