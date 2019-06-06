package com.mobile.usoz.Calendar.Notes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> mNotes = new ArrayList<>();
    private Context mContext;
    private  String day,month;

    private FirebaseAuth mAuth;

    // ------------------------------ RecyclerView setup ----------------------------------------

    public EventsRecyclerViewAdapter(Context context, ArrayList<String> events, String day, String month){
        mContext = context;
        mNotes = events;
        this.day = day;
        this.month = month;

        mAuth = FirebaseAuth.getInstance();
    }
    @NonNull
    @Override
    public EventsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_recycler_view_item, viewGroup, false);
        return new EventsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRecyclerViewAdapter.ViewHolder viewHolder, int i) {
        viewHolder.noteTextView.setText(mNotes.get(i));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }


    // ----------------------------- ViewHolder  ------------------------------------------------

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView noteTextView;
        private RelativeLayout parentLayout;
        private ImageView deleteNoteImageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.note_recycler_view_item_layout);
            noteTextView = itemView.findViewById(R.id.note_recycler_view_item_text_view);
            deleteNoteImageView = itemView.findViewById(R.id.note_recycler_view_item_image_view);
            deleteNoteImageView.setVisibility(View.INVISIBLE);
        }
    }

}
