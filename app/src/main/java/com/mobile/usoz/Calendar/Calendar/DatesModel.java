package com.mobile.usoz.Calendar.Calendar;

import java.util.ArrayList;
import java.util.List;

public class DatesModel {
    public ArrayList<String> mDates;
    public ArrayList<String> mEvents;
    public String month;

    DatesModel(){
        mDates = new ArrayList<>();
        mEvents= new ArrayList<>();
    }
}
