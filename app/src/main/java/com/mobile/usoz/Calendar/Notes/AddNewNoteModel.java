package com.mobile.usoz.Calendar.Notes;

import com.mobile.usoz.Calendar.Calendar.CalendarModel;

import java.util.ArrayList;
import java.util.List;

public class AddNewNoteModel {
    public String month = "Styczeń";
    public String day = "1";
    public List<String> listOfNotes;
    public final List<String> months;
    public List<String> days;
    public AddNewNoteModel(){
        listOfNotes = new ArrayList<>();
        days = new ArrayList<>();
        months = new ArrayList<>();
        months.add("Styczeń");
        months.add("Luty");
        months.add("Marzec");
        months.add("Kwiecień");
        months.add("Maj");
        months.add("Czerwiec");
        months.add("Lipiec");
        months.add("Sierpień");
        months.add("Wrzesień");
        months.add("Październik");
        months.add("Listopad");
        months.add("Grudzień");
    }
}
