package com.mobile.usoz.Calendar.Calendar;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobile.usoz.Calendar.Notes.DisplayNotesActivity;
import com.mobile.usoz.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.text.ParseException;
import java.util.Locale;

public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<CalendarRecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "CalendarRecyclerViewAdapter";

    private ArrayList<String> data ;
    private Context mContext;
    public static final String CALENDAR_EXTRA_TEXT = "com.mobile.usoz.Calendar.Notes.CALENDAR_EXTRA_TEXT";
    public static final String DATES_MONTH_EXTRA_TEXT = "com.mobile.usoz.Calendar.Notes.MONTH_EXTRA_TEXT";
    public static final String DATES_DAY_EXTRA_TEXT = "com.mobile.usoz.Calendar.Notes.DAY_EXTRA_TEXT";

    private String  month;
    public CalendarRecyclerViewAdapter(Context context, ArrayList<String> dates){
        data = dates;
        mContext = context;
    }
    public CalendarRecyclerViewAdapter(Context context, List<String> dates){
        data = (ArrayList<String>) dates;
        mContext = context;
    }
    public CalendarRecyclerViewAdapter(Context context, ArrayList<String> dates, String month ) {
        data =  dates;
        mContext = context;
        this.month = month;
    }
    public CalendarRecyclerViewAdapter(Context context, ArrayList<String> dates, ArrayList<String> events, String month ){
        parseList(dates, events);
        mContext = context;
        this.month = month;
    }

    private void parseList(ArrayList<String> dates, ArrayList<String> events) {
        data = dates;
        for(String s : events) {
            if(!data.contains(s)) {
                data.add(s);
            }
        }
    }

    // Przypisanie ViewHoldera który będzie spinał recyclerView
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_recycle_view_item, parent, false);
        return  new ViewHolder(view);
    }


    // Przypisanie danych do wiersza na danej pozycji
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        // TODO: ADD NAME OF DAY
        if(mContext instanceof CalendarActivity){
            // Jeżeli nasz parentlayout to Callendar wtedy po prostu ustawiamy date
            viewHolder.dateTextView.setText(data.get(position));
        } else {
            // Jeśli nasz parenlayout to DatesActivity to ustawiamy dni tygodnia w których są notatki
            String date = getDayName(position);
            viewHolder.dateTextView.setText(date);
        }

        // Ustawiamy listener na kliknięcie w dany wiersz w recyclerView
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(mContext instanceof CalendarActivity){
                    intent = new Intent(mContext, DatesActivity.class);
                    String s = formatMonthToNumber(data.get(position));
                    intent.putExtra(CALENDAR_EXTRA_TEXT, s);
                } else {
                    intent = new Intent(mContext, DisplayNotesActivity.class);
                    intent.putExtra(DATES_DAY_EXTRA_TEXT, data.get(position));
                    intent.putExtra(DATES_MONTH_EXTRA_TEXT, month);
                }

                mContext.startActivity(intent);
            }
        });
    }

    // Funkcja zwraca nazwe dnia  tygodnia
    private String getDayName(final int position){
        String date  = data.get(position);
        String dateString = String.format("%d-%d-%d", 2019, Integer.parseInt(month), Integer.parseInt(date));
        Date inputDate = new Date();
        try {
            inputDate = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        } catch (ParseException exception) {
            System.out.print(exception);
        }
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(inputDate);
        date = date + ".  " + translateEnglishToPolishNameOfDay(dayOfWeek);
        return date;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.calendar_cell_text_view);
            parentLayout = itemView.findViewById(R.id.calendar_recycle_view_item_layout);
        }
    }

    // Funkcje pomocnicze formatujące miesiące na indexy lub indexy na miesiace

    private String formatMonthToNumber(String month){
        switch (month){
            case "Styczeń":
                return "1";
            case "Luty":
                return "2";
            case "Marzec":
                return "3";
            case "Kwiecień":
                return "4";
            case "Maj":
                return "5";
            case "Czerwiec":
                return "6";
            case "Lipiec":
                return "7";
            case "Sierpień":
                return "8";
            case "Wrzesień":
                return "9";
            case "Październik":
                return "10";
            case "Listopad":
                return "11";
            case "Grudzień":
                return "12";
            default:
                return "0";
        }
    }

    public static String formatNumberToMonth(int i) {
        switch (i) {
            case 1:
                return "Styczeń";
            case 2:
                return "Luty";
            case 3:
                return "Marzec";
            case 4:
                return "Kwiecień";
            case 5:
                return "Maj";
            case 6:
                return "Czerwiec";
            case 7:
                return "Lipiec";
            case 8:
                return "Sierpień";
            case 9:
                return "Wrzesień";
            case 10:
                return "Październik";
            case 11:
                return "Listopad";
            case 12:
                return "Grudzień";
        }
        return "none";
    }

    public static String formatDateToDayOfWeek(String month, String day) {
        String dateString = String.format("%d-%d-%d", 2019, Integer.parseInt(month), Integer.parseInt(day));
        Date inputDate = new Date();
        try {
            inputDate = new SimpleDateFormat("yyyy-M-d").parse(dateString);
        } catch (ParseException exception) {
            System.out.print(exception);
        }
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(inputDate);
        return translateEnglishToPolishNameOfDay(dayOfWeek);
    }

    private static String translateEnglishToPolishNameOfDay(String name) {
        String name1 = name.toLowerCase();
        switch (name1) {
            case "monday":
                return "Poniedziałek";
            case "tuesday":
                return "Wtorek";
            case "wednesday":
                return "Środa";
            case "thursday":
                return "Czwartek";
            case "friday":
                return "Piątek";
            case "saturday":
                return "Sobota";
            case "sunday":
                return "Niedziela";
        }
        return "none";
    }
}

