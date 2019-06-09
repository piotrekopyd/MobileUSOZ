package com.mobile.usoz.CollectiveMethods;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.mobile.usoz.LecturersActivities.LecturersActivity;
import com.mobile.usoz.Maps.MapsActivity;

public class CollectiveMethods {
    public static BroadcastReceiver setupReciever(final CollectiveMethodsCallback callback,
                                     final ConnectivityManager connectivityManager,
                                     final Object modelReference,
    final Context context) {
        BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (isNetworkAvailable(connectivityManager)) {
                    if (modelReference == null) {
                        Toast.makeText(context, "Sieć znów działa", Toast.LENGTH_SHORT).show();
                        callback.onDownload(true);
                    }
                } else if (modelReference == null) {
                    if(context instanceof MapsActivity) {
                        Toast.makeText(context, "Wystąpił błąd podczas ładowania listy miejsc. Spróbuj ponownie za chwilę", Toast.LENGTH_LONG).show();
                    } else if(context instanceof LecturersActivity){
                        Toast.makeText(context, "Nie udało się pobrać listy prowadzących", Toast.LENGTH_LONG).show();
                    }
                }
                callback.onDownload(false);
            }
        };
        return networkChangeReceiver;
    }
    public static boolean isNetworkAvailable(ConnectivityManager connectivityManager) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public static boolean isNetworkAvailableForToast(ConnectivityManager connectivityManager, Context context) {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if(!isAvailable) {
            if(context instanceof LecturersActivity) {
                Toast.makeText(context, "Nie udało się pobrać ocen wybranego prowadzącego", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Brak połączenia z siecią", Toast.LENGTH_SHORT).show();
            }
        }
        return isAvailable;
    }
}