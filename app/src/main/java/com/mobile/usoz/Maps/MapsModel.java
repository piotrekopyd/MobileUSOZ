package com.mobile.usoz.Maps;

import java.util.LinkedList;

public class MapsModel {
    public LinkedList<MyMarker> myMarkersCollection;
    public int mSelectedIndex;
    public MapsModel() {
        myMarkersCollection = null;
        mSelectedIndex = 0;
    }
}
