package com.mobile.usoz.Calendar.Notes;

import java.util.ArrayList;
import java.util.List;

public class AddNewNoteModel {
    public String month;
    public String day;
    public List<String> list;

    AddNewNoteModel(){
        list = new ArrayList<>();
    }
}
