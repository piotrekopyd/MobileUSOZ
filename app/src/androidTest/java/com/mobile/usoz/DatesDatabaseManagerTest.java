package com.mobile.usoz;

import com.mobile.usoz.DatabaseManager.DatesDatabaseManager;
import com.mobile.usoz.DatabaseManager.Protocols.DatesDatabaseManagerInterface;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DatesDatabaseManagerTest {
    private DatesDatabaseManager manager;
    private DatesDatabaseManagerInterface interf = (list) -> {throw new NullPointerException();};

    @Test
    public void retrieveDatesFromFirebase_null() {
        try {
            manager.retrieveDatesFromFirebase("1", null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void retrieveDatesFromFirebase_wrongInterface() {
        try {
            manager.retrieveDatesFromFirebase("1", interf);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void retrieveEventsFromFirebase_null() {
        try {
            manager.retrieveEventsFromFirebase("1", null);
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void retrieveEventsFromFirebase_wrongInterface() {
        try {
            manager.retrieveEventsFromFirebase("1", (list) -> {});
            fail();
        }
        catch(NullPointerException e) {
            assertTrue(true);
        }
    }
}
