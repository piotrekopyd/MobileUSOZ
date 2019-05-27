package com.mobile.usoz.DatabaseManager;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mobile.usoz.Calendar.Notes.DisplayNotesActivity;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.NotesDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.Protocols.DatesDatabaseManagerInterface;
import com.mobile.usoz.DatabaseManager.Protocols.DisplayNotesDatabaseManagerInterface;

import java.util.ArrayList;

public class DisplayNotesDatabaseManager extends DatabaseManager implements NotesDatabaseKeyValues {

    public void fetchNotesFromFirebase(final String day, final String month, final DisplayNotesDatabaseManagerInterface callback){
        db.collection(NOTES_COLLECTION_PATH).document(user.getUid()).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ArrayList<String> notes = (ArrayList<String>)document.get(KEY_NOTE);
                            callback.prepareArray(notes);
                            //fetchEventsFromFirebase();
                        } else {
                            callback.prepareArray(null);
                            //Toast.makeText(DisplayNotesActivity.this,"Error1", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ----------------------------- Fetch events from database ---------------------

    public void fetchEventsFromFirebase(final String day, final String month, final DisplayNotesDatabaseManagerInterface callback){
        db.collection(NOTES_COLLECTION_PATH).document(EVENTS_COLLECTION_PATH).collection(month).document(day).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            ArrayList<String> note = (ArrayList<String>)document.get(KEY_NOTE);
                            callback.prepareArray(note);
                            //fetchEventsFromFirebase();
                        } else {
                            //Toast.makeText(DisplayNotesActivity.this,"Error2", Toast.LENGTH_SHORT).show();
                            callback.prepareArray(null);
                        }
                    }
                });
    }
}
