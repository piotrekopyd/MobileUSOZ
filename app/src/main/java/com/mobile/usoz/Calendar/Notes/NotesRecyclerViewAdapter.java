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
import com.mobile.usoz.Calendar.Notes.NotesRecyclerViewAdapter;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> mNotes = new ArrayList<>();
    private Context mContext;

    public NotesRecyclerViewAdapter(Context context, ArrayList<String> notes){
        mContext = context;
        mNotes = notes;
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView noteTextView;
        private ImageView deleteNoteImageView;
        private RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parentLayout = itemView.findViewById(R.id.note_recycler_view_item_layout);
            noteTextView = itemView.findViewById(R.id.note_recycler_view_item_text_view);
            deleteNoteImageView = itemView.findViewById(R.id.note_recycler_view_item_image_view);
        }
    }
}
