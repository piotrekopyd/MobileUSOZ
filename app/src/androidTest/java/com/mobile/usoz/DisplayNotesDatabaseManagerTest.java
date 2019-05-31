package com.mobile.usoz;

import com.mobile.usoz.DatabaseManager.DisplayNotesDatabaseManager;
import com.mobile.usoz.DatabaseManager.Protocols.DisplayNotesDatabaseManagerInterface;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DisplayNotesDatabaseManagerTest {
    DisplayNotesDatabaseManager manager;
    DisplayNotesDatabaseManagerInterface interf = (list) -> {throw new NullPointerException();};

    @Test
    public void fetchNotesFromFirebase_nullInterf() {
        try {
            manager.fetchNotesFromFirebase("1", "1", null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void fetchNotesFromFirebase_nullAll() {
        try {
            manager.fetchNotesFromFirebase(null, null, null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void fetchNotesFromFirebase_wrongInterface() {
        try {
            manager.fetchNotesFromFirebase("1", "1", interf);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void fetchEventsFromFirebase_nullInterf() {
        try {
            manager.fetchEventsFromFirebase("1", "1", null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void fetchEventsFromFirebase_nullAll() {
        try {
            manager.fetchEventsFromFirebase(null, null, null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void fetchEventsFromFirebase_wrongInterface() {
        try {
            manager.fetchEventsFromFirebase("1", "1", interf);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }
}
