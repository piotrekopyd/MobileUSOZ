package com.mobile.usoz.DatabaseManager;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.mobile.usoz.DatabaseManager.FirebaseKeyValues.UserDataDatabaseKeyValues;
import com.mobile.usoz.DatabaseManager.Protocols.UserProfileShowToastDatabaseManagrInterface;
import com.mobile.usoz.DatabaseManager.Protocols.UserProfileRetrieveDataInterface;
import com.mobile.usoz.UserActivities.UserProfileAcitivity;

import java.util.HashMap;
import java.util.Map;

public class UserProfileDatabaseManager extends DatabaseManager implements UserDataDatabaseKeyValues {
    // Save edited user data to firebase
    public void saveData(String name, String lastName, String university, String birthday, String passions, final UserProfileShowToastDatabaseManagrInterface callback) {
        Map<String, Object> newUserData = new HashMap<>();
        newUserData.put(KEY_NAME, name);
        newUserData.put(KEY_LASTNAME, lastName);
        newUserData.put(KEY_UNIVERSITY, university);
        newUserData.put(KEY_DATEOFBIRTH,birthday);
        newUserData.put(KEY_PASSIONS, passions);

        db.collection("User data").document(user.getUid().toString()).set(newUserData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        callback.showToast(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.showToast(false);
                    }
                });
    }
    // Function retrieves user data from firebase
    // Callback function fills text views with fetched parameters
    public void retrieveUserData(final UserProfileRetrieveDataInterface callback, final UserProfileShowToastDatabaseManagrInterface showToast){
        db.collection ("User data").document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            String name = documentSnapshot.getString(KEY_NAME);
                            String lastName = documentSnapshot.getString(KEY_LASTNAME);
                            //nameTextView.setText(name + " " + lastName);
                            String university = documentSnapshot.getString(KEY_UNIVERSITY);
                            String birthday = documentSnapshot.getString(KEY_DATEOFBIRTH);
                            String email = user.getEmail();
                            String passions = documentSnapshot.getString(KEY_PASSIONS);
                            callback.retrieveData(name,lastName,university,birthday,email,passions);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast.showToast(false);
                    }
                });
    }
}
