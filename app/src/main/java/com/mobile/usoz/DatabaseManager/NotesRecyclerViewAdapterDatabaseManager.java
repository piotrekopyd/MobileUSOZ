package com.mobile.usoz.DatabaseManager;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.Protocols.NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface;
import com.mobile.usoz.DatabaseManager.Protocols.NotesRecyclerViewAdapterDatabaseManagerDeleteNoteInterface;

import java.util.ArrayList;

public class NotesRecyclerViewAdapterDatabaseManager extends DatabaseManager implements NotesDatabaseKeyValues {
    public void deleteItemFromDatabase(final String note, final String day, final String month, final int position, final NotesRecyclerViewAdapterDatabaseManagerDeleteNoteInterface callback){
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).update(KEY_NOTE, FieldValue.arrayRemove(note));
                            callback.deleteNoteFromModel(position);
//                            mNotes.remove(position);
//                            notifyItemRemoved(position);
//                            deleteDate();
                        } else {
                            callback.deleteNoteFromModel(-1);
                        }
                    }
                });
    }

    public void deleteDate(final String day, final String month, final NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface callback){
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        // If deleted note was last one at particular month we need to delete date from dates list at Firestore
                        // Callback function takes us back to calendar activity
                        if(document.exists()){
                            ArrayList<String> list = (ArrayList<String>)document.get(KEY_NOTE);
                            if(list.isEmpty()){
                                isAnyEvent(day, month, callback);
                            } else {
                                callback.backToCalendarView(false);
                            }
                        } else {
                            callback.backToCalendarView(false);
                        }
                    }
                });
    }
    private void isAnyEvent(final String day, final String month, final NotesRecyclerViewAdapterDatabaseManagerDeleteDateInterface callback){
        db.collection(NOTES_COLLECTION_PATH).document("Events").collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        // If deleted note was last one at particular month we need to delete date from dates list at Firestore
                        // Callback function takes us back to calendar activity
                        if(document.exists()){
                            ArrayList<String> list = (ArrayList<String>)document.get(KEY_NOTE);
                            if(list.isEmpty()){
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayRemove(day));
                                callback.backToCalendarView(true);
                            } else {
                                callback.backToCalendarView(false);
                            }
                        } else {
                            db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayRemove(day));
                            callback.backToCalendarView(true);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.backToCalendarView(true);
                    }
                });
    }
}
