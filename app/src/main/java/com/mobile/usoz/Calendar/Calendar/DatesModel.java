package com.mobile.usoz.Calendar.Calendar;

import java.util.ArrayList;

public class DatesModel {
    public ArrayList<String> mDates;
    public String month;

    DatesModel(){
        mDates = new ArrayList<>();
        getDates();
    }

    private void getDates(){
        mDates.add("10-" + month);
        mDates.add("11-" + month);
    }
}
