package com.mobile.usoz;

import com.mobile.usoz.Calendar.Calendar.CalendarModel;
import com.mobile.usoz.Calendar.Notes.AddNewNoteModel;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AddNewNoteModelTest {
    AddNewNoteModel model = new AddNewNoteModel();

    @Test
    public void List_Styczen() {
        boolean result = model.months.contains("Styczeń");
        assertTrue(result);
    }

    @Test
    public void List_Luty() {
        boolean result = model.months.contains("Luty");
        assertTrue(result);
    }

    @Test
    public void List_Marzec() {
        boolean result = model.months.contains("Marzec");
        assertTrue(result);
    }

    @Test
    public void List_Kwiecien() {
        boolean result = model.months.contains("Kwiecień");
        assertTrue(result);
    }

    @Test
    public void List_Maj() {
        boolean result = model.months.contains("Maj");
        assertTrue(result);
    }

    @Test
    public void List_Czerwiec() {
        boolean result = model.months.contains("Czerwiec");
        assertTrue(result);
    }

    @Test
    public void List_Lipiec() {
        boolean result = model.months.contains("Lipiec");
        assertTrue(result);
    }

    @Test
    public void List_Sierpien() {
        boolean result = model.months.contains("Sierpień");
        assertTrue(result);
    }

    @Test
    public void List_Wrzesien() {
        boolean result = model.months.contains("Wrzesień");
        assertTrue(result);
    }

    @Test
    public void List_Pazdziernik() {
        boolean result = model.months.contains("Październik");
        assertTrue(result);
    }

    @Test
    public void List_Listopad() {
        boolean result = model.months.contains("Listopad");
        assertTrue(result);
    }

    @Test
    public void List_Grudzien() {
        boolean result = model.months.contains("Grudzień");
        assertTrue(result);
    }

    @Test
    public void List_Empty() {
        boolean result = model.months.contains("");
        assertFalse(result);
    }

    @Test
    public void List_StyczenBezPolskichZnakow() {
        boolean result = model.months.contains("Styczen");
        assertFalse(result);
    }

    @Test
    public void List_Wrong() {
        boolean result = model.months.contains("abcd");
        assertFalse(result);
    }

    @Test
    public void List_Eng() {
        boolean result = model.months.contains("June");
        assertFalse(result);
    }
}
