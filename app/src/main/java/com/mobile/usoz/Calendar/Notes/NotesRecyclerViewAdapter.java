package com.mobile.usoz.Calendar.Notes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.NotesRecyclerViewAdapterDatabaseManager;
import com.mobile.usoz.DatabaseManager.Protocols.NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface;
import com.mobile.usoz.DatabaseManager.Protocols.NotesRecyclerViewAdapterDatabaseManagerDeleteNoteInterface;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> implements NotesDatabaseKeyValues {
    private ArrayList<String> mNotes = new ArrayList<>();
    private Context mContext;
    private  String day,month;
    private NotesRecyclerViewAdapterDatabaseManager databaseManager;

    // ------------------------------ RecyclerView setup ----------------------------------------

    public NotesRecyclerViewAdapter(Context context, ArrayList<String> notes, String day, String month){
        mContext = context;
        mNotes = notes;
        this.day = day;
        this.month = month;
        databaseManager = new NotesRecyclerViewAdapterDatabaseManager();


    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_recycler_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.noteTextView.setText(mNotes.get(i));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    // ----------------------------- ViewHolder  ------------------------------------------------

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView noteTextView;
        private ImageView deleteNoteImageView;
        private RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.note_recycler_view_item_layout);
            noteTextView = itemView.findViewById(R.id.note_recycler_view_item_text_view);
            deleteNoteImageView = itemView.findViewById(R.id.note_recycler_view_item_image_view);

            deleteNoteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getPosition();
                    deleteNote(position);
                }
            });
        }
    }

    private void deleteNote(int position){
        databaseManager.deleteItemFromDatabase(mNotes.get(position), day, month, position, new NotesRecyclerViewAdapterDatabaseManagerDeleteNoteInterface() {
            @Override
            public void deleteNoteFromModel(int position) {
                if( position >= 0) {
                    mNotes.remove(position);
                    notifyItemRemoved(position);
                    databaseManager.deleteDate(day, month, new NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface() {
                        @Override
                        public void backToCalendarView(boolean isTrue) {
                            if (isTrue)
                                backToCalendar();
                        }
                    });
                } else {
                    Toast.makeText(mContext, "Błąd podczas usuwania", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backToCalendar(){
        Intent intent = new Intent(mContext, CalendarActivity.class);
        mContext.startActivity(intent);
    }
}
