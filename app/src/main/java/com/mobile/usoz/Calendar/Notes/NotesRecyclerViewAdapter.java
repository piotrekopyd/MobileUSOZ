package com.mobile.usoz.Calendar.Notes;

import android.app.Activity;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mobile.usoz.Calendar.Calendar.CalendarActivity;
import com.mobile.usoz.Interfaces.NotesDatabaseKeyValues;
import com.mobile.usoz.R;

import java.util.ArrayList;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder> implements NotesDatabaseKeyValues {
    private ArrayList<String> mNotes = new ArrayList<>();
    private Context mContext;
    private  String day,month;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore db ;
    // ------------------------------ RecyclerView setup ----------------------------------------

    public NotesRecyclerViewAdapter(Context context, ArrayList<String> notes, String day, String month){
        mContext = context;
        mNotes = notes;
        this.day = day;
        this.month = month;

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
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
                    deleteItemFromDatabase(day,month,position);

                }
            });
        }
    }

    private void deleteItemFromDatabase(final String day,final String month, final int position){
        // Delete note
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).update(KEY_NOTE, FieldValue.arrayRemove(mNotes.get(position)));
                            mNotes.remove(position);
                            notifyItemRemoved(position);
                            deleteDate();
                        } else {
                            Toast.makeText(mContext,"Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void deleteDate(){
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ArrayList<String> list = (ArrayList<String>)document.get(KEY_NOTE);
                            if(list.isEmpty()){
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayRemove(day));
                                backToCalendar();
                            }
                        } else {
                            // db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayRemove(day));
                        }
                    }
                });
    }
    private void backToCalendar(){
        Intent intent = new Intent(mContext, CalendarActivity.class);
        mContext.startActivity(intent);
    }
}
