package com.mobile.usoz.Maps;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;

final class MyMarker implements Serializable {
    private double latitude;
    private double longitude;
    private String title;
    private String snippet;
    private float color;
    public MyMarker(double la, double lo, String ti, String sn, float co) {
        latitude = la;
        longitude = lo;
        title = ti;
        snippet = sn;
        color = co;
    }
    public MarkerOptions getMarkerOptions() {
        MarkerOptions markerOptions = new MarkerOptions();
        return markerOptions.position(new LatLng(latitude, longitude)).title(title).snippet(snippet).icon(BitmapDescriptorFactory.defaultMarker(color));
    }

    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public void setColor(float c) {
        color = c;
    }

    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getTitle() {
        return title;
    }
    public String getSnippet() {
        return snippet;
    }
    public float getColor() {
        return color;
    }
}