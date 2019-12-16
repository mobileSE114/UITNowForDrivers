package com.uit.uitnowfordrivers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;

public class LocationServiceTask {
    public static boolean isLocationServiceEnabled(Context context) {
        boolean isGPSEnabled;
        boolean isNetworkEnabled;
        LocationManager locManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGPSEnabled = true;
        } else {
            isGPSEnabled = false;
        }
        if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            isNetworkEnabled = true;
        } else {
            isNetworkEnabled = false;
        }
        return isGPSEnabled || isNetworkEnabled;
    }
    public static void displayEnableLocationServiceDialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final String action = Settings.ACTION_LOCATION_SOURCE_SETTINGS;final String message = "Please enable Location Services to detect your location.";

        builder.setMessage(message)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                context.startActivity(new Intent(action));
                                d.dismiss();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface d, int id) {
                                d.cancel();
                            }
                        });
        builder.create().show();
    }
    public static String getAddressFromLatLng(Context context, GeoPoint geoPoint) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses;
        Address address;
        String addressText = "";
        try {
            addresses = geocoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses.get(0);
                if (address.getMaxAddressLineIndex() > 0) {
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        addressText += (i == 0) ? address.getAddressLine(i) : "\n" +
                                address.getAddressLine(i);
                    }
                } else {
                    addressText = address.getAddressLine(0);
                }
            }
        } catch (IOException e) {
            Log.e("Test", e.getLocalizedMessage());
        }
        return addressText;
    }
}
