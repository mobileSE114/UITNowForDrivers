package com.uit.uitnowfordrivers;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.GeoPoint;

public class MessageEvent {
    static final String FROM_requestACT_TO_trackingACT = "FROM_requestACT_TO_trackingACT";
    GeoPoint store,user,driver;
    Request request;
    String type;
    public MessageEvent(GeoPoint store,GeoPoint user, GeoPoint driver,Request request,String type)
    {
        this.store=store;
        this.user=user;
        this.driver=driver;
        this.request=request;
        this.type=type;
    }

}
