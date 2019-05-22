package com.mobile.usoz;

import com.mobile.usoz.Calendar.Calendar.CalendarModel;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CalendarModelTest {
    CalendarModel model = new CalendarModel();

    @Test
    public void List_Styczen() {
        boolean result = model.mMonths.contains("Styczeń");
        assertTrue(result);
    }

    @Test
    public void List_Luty() {
        boolean result = model.mMonths.contains("Luty");
        assertTrue(result);
    }

    @Test
    public void List_Marzec() {
        boolean result = model.mMonths.contains("Marzec");
        assertTrue(result);
    }

    @Test
    public void List_Kwiecien() {
        boolean result = model.mMonths.contains("Kwiecień");
        assertTrue(result);
    }

    @Test
    public void List_Maj() {
        boolean result = model.mMonths.contains("Maj");
        assertTrue(result);
    }

    @Test
    public void List_Czerwiec() {
        boolean result = model.mMonths.contains("Czerwiec");
        assertTrue(result);
    }

    @Test
    public void List_Lipiec() {
        boolean result = model.mMonths.contains("Lipiec");
        assertTrue(result);
    }

    @Test
    public void List_Sierpien() {
        boolean result = model.mMonths.contains("Sierpień");
        assertTrue(result);
    }

    @Test
    public void List_Wrzesien() {
        boolean result = model.mMonths.contains("Wrzesień");
        assertTrue(result);
    }

    @Test
    public void List_Pazdziernik() {
        boolean result = model.mMonths.contains("Październik");
        assertTrue(result);
    }

    @Test
    public void List_Listopad() {
        boolean result = model.mMonths.contains("Listopad");
        assertTrue(result);
    }

    @Test
    public void List_Grudzien() {
        boolean result = model.mMonths.contains("Grudzień");
        assertTrue(result);
    }

    @Test
    public void List_Empty() {
        boolean result = model.mMonths.contains("");
        assertFalse(result);
    }

    @Test
    public void List_StyczenBezPolskichZnakow() {
        boolean result = model.mMonths.contains("Styczen");
        assertFalse(result);
    }

    @Test
    public void List_Wrong() {
        boolean result = model.mMonths.contains("abcd");
        assertFalse(result);
    }

    @Test
    public void List_Eng() {
        boolean result = model.mMonths.contains("June");
        assertFalse(result);
    }
}
