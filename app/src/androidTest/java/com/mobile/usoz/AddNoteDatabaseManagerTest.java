package com.mobile.usoz;

import com.mobile.usoz.DatabaseManager.AddNoteDatabaseManager;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AddNoteDatabaseManagerTest {
    AddNoteDatabaseManager manager;

    @Test
    public void saveNote_null1() {
        try {
            manager.saveNote(null, "1", "1");
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void saveNote_null2() {
        try {
            manager.saveNote("temp", null, "1");
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void saveNote_null3() {
        try {
            manager.saveNote("temp", "1", null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void saveNote_nullAll() {
        try {
            manager.saveNote(null, null, null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }
}
