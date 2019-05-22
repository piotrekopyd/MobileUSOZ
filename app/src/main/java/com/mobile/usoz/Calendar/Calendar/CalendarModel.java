package com.mobile.usoz.Calendar.Calendar;

import android.util.Log;

import java.util.ArrayList;

public class CalendarModel {
    public ArrayList<String> mMonths = new ArrayList<>();

    public CalendarModel(){
        initDates();
    }

    private void initDates(){
        mMonths.add("Styczeń");
        mMonths.add("Luty");
        mMonths.add("Marzec");
        mMonths.add("Kwiecień");
        mMonths.add("Maj");
        mMonths.add("Czerwiec");
        mMonths.add("Lipiec");
        mMonths.add("Sierpień");
        mMonths.add("Wrzesień");
        mMonths.add("Październik");
        mMonths.add("Listopad");
        mMonths.add("Grudzień");
    }
}
