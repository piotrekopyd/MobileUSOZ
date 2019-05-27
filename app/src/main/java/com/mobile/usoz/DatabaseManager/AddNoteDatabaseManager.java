package com.mobile.usoz.DatabaseManager;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.mobile.usoz.Calendar.Notes.AddNewNoteActivity;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;

import java.util.HashMap;
import java.util.Map;

public class AddNoteDatabaseManager extends DatabaseManager implements NotesDatabaseKeyValues {

    public void saveNote(final String noteText, final String day, final String month){
        final Map<String, Object> note = new HashMap<>();
        note.put(KEY_NOTE, noteText);
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).update(KEY_NOTE, FieldValue.arrayUnion(noteText));
                            } else {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).set(note);
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).update(KEY_NOTE, FieldValue.arrayUnion(noteText));
                            }
                        } else {
                            //Toast.makeText(AddNewNoteActivity.this, "DATABASE ERROR!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayUnion(day));
                            } else {
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).set(note);
                                db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(KEY_NUMBERS_OF_DAY_DOCUMENT).update(KEY_NOTE, FieldValue.arrayUnion(day));
                            }
                        } else {
                            //Toast.makeText(AddNewNoteActivity.this, "Błąd bazy danych!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
