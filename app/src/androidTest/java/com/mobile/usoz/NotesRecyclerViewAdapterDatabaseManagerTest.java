package com.mobile.usoz;

import com.mobile.usoz.DatabaseManager.NotesRecyclerViewAdapterDatabaseManager;
import com.mobile.usoz.DatabaseManager.Protocols.NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface;
import com.mobile.usoz.DatabaseManager.Protocols.NotesRecyclerViewAdapterDatabaseManagerDeleteNoteInterface;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class NotesRecyclerViewAdapterDatabaseManagerTest {
    private NotesRecyclerViewAdapterDatabaseManager manager;
    private NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface dateInterface = (list) -> {throw new NullPointerException();};
    private NotesRecyclerViewAdapterDatabaseManagerDeleteNoteInterface noteInterface = (list) -> {throw new NullPointerException();};

    @Test
    public void deleteItemFromDatabase_nullInterf() {
        try {
            manager.deleteItemFromDatabase("temp", "1", "1", 1, null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteItemFromDatabase_nullMore() {
        try {
            manager.deleteItemFromDatabase(null, "1", null, 1, null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteItemFromDatabase_wrongInterf() {
        try {
            manager.deleteItemFromDatabase("temp", "1", "1", 1, noteInterface);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteDate_nullInterf() {
        try {
            manager.deleteDate("1", "1", null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteDate_nullAll() {
        try {
            manager.deleteDate(null, null, null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void deleteDate_wrongInterf() {
        try {
            manager.deleteDate("temp", "1", dateInterface);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }
}
