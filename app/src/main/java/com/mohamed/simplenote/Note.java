package com.mohamed.simplenote;

import java.io.Serializable;

// Implements Serializable, So you can pass it using Intent.
public class Note implements Serializable {

    private int noteId;
    private String noteTitle;
    private String noteContent;
    private String noteDate;

    public Note()  {

    }

    public Note(String noteTitle, String noteContent, String noteDate) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteDate = noteDate;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }
}
