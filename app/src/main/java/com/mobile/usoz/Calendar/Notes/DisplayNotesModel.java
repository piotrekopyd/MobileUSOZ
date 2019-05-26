package com.mobile.usoz.Calendar.Notes;

import java.util.ArrayList;

public class DisplayNotesModel {
    public ArrayList<String> mNotes;
    public ArrayList<String> mEvents;
    public String month;
    public String day;

    DisplayNotesModel(){
        mNotes = new ArrayList<>();
        mEvents = new ArrayList<>();
    }
}
