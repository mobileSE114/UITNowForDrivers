package com.uit.uitnowfordrivers;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.GeoPoint;

public class App extends Application {
    Driver driver;
    GeoPoint location;
    Request currentRequest;
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
