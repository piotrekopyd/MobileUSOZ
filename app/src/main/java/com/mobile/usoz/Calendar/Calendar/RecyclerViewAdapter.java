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
import java.text.SimpleDateFormat;
import java.util.Locale;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<String> data ;
    private Context mContext;
    public static final String CALENDAR_EXTRA_TEXT = "com.mobile.usoz.Calendar.Notes.CALENDAR_EXTRA_TEXT";
    public static final String DATES_MONTH_EXTRA_TEXT = "com.mobile.usoz.Calendar.Notes.MONTH_EXTRA_TEXT";
    public static final String DATES_DAY_EXTRA_TEXT = "com.mobile.usoz.Calendar.Notes.DAY_EXTRA_TEXT";

    private String  month;
    public RecyclerViewAdapter( Context context,ArrayList<String> dates){
        data = dates;
        mContext = context;
    }
    public RecyclerViewAdapter(Context context, List<String> dates){
        data = (ArrayList<String>) dates;
        mContext = context;
    }
    public RecyclerViewAdapter(Context context, ArrayList<String> dates, String month ){
        data =  dates;
        mContext = context;
        this.month = month;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_recycle_view_item, parent, false);
        return  new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");

        // TODO: ADD NAME OF DAY
        if(mContext instanceof CalendarActivity){
            viewHolder.dateTextView.setText(data.get(position));
        } else {
            String date = data.get(position);
            String dateString = String.format("%d-%d-%d", 2019, Integer.parseInt(month), Integer.parseInt(date));
            Date inputDate = new Date();
            try {
                inputDate = new SimpleDateFormat("yyyy-M-d").parse(dateString);
            } catch (ParseException exception) {
                System.out.print(exception);
            }
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(inputDate);
            date = date + ".  " + dayOfWeek;
            viewHolder.dateTextView.setText(date);
        }


        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(mContext instanceof CalendarActivity){
                    intent = new Intent(mContext, DatesActivity.class);
                    String s = formatMonthToNumber(data.get(position));
                    intent.putExtra(CALENDAR_EXTRA_TEXT, s);
                }else{
                    intent = new Intent(mContext, DisplayNotesActivity.class);
                    intent.putExtra(DATES_DAY_EXTRA_TEXT, data.get(position));
                    intent.putExtra(DATES_MONTH_EXTRA_TEXT, month);
                }

                mContext.startActivity(intent);
            }
        });
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
            case "Lispotad":
                return "11";
            case "Grudzień":
                return "12";
            default:
                return "0";
        }
    }
}
