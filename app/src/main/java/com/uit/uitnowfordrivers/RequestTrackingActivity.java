package com.uit.uitnowfordrivers;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class RequestTrackingActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {
    RelativeLayout layoutRequesting, layoutInfor;
    App app;
    GoogleMap map;
    TextView tvDelivery, tvStore, tvTotal;
    Button btnCancelRequest,btnFinish;
    FirebaseFirestore db;
    Request request;
    FusedLocationProviderClient locationClient;
    LocationCallback locationCallback;
    static int REQUEST_LOCATION = 101;
    long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    long FASTEST_INTERVAL = 5 * 1000; /* 2 sec */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        startLocationUpdates();
        placeUserMarkerOnMap();
        placeStoreMarkerOnMap();
     //   placeDriverMarkerOnMap();
    }

    private void placeUserMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(request.userLocation.getLatitude(),request.userLocation.getLongitude()));
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_location)));
        options.title(request.getUserName());
        map.addMarker(options);
    }

    private void placeStoreMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(request.storeLocation.getLatitude(),request.storeLocation.getLongitude()));
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(),R.drawable.ic_marker)));
        options.title(request.getStoreName());
        map.addMarker(options);
      //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(request.storeLocation.getLatitude(),request.storeLocation.getLongitude()),14));
    }

    private void placeDriverMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(request.driverLocation.getLatitude(), request.driverLocation.getLongitude())).title(app.driver.getName());
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_motor)));
        map.addMarker(options);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(request.driverLocation.getLatitude(),request.driverLocation.getLongitude()),14));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_tracking);
        db=FirebaseFirestore.getInstance();
        app = (App) getApplication();
        request=app.currentRequest;
        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.frgMaps);
        mapFragment.getMapAsync(this);
        tvDelivery = findViewById(R.id.tvCus);
        tvStore = findViewById(R.id.tvStore);
        tvTotal = findViewById(R.id.tvTotal);
        layoutRequesting = findViewById(R.id.layoutRequesting);
        layoutInfor = findViewById(R.id.layoutInfo);
        btnCancelRequest=findViewById(R.id.btnCancel);
        btnCancelRequest.setOnClickListener(this);
        tvDelivery.setText(request.getUserAddress());
        tvStore.setText(request.getStoreAddress());
        tvTotal.setText(request.getTotal());
        tvStore.setOnClickListener(this);
        tvDelivery.setOnClickListener(this);
        btnFinish=findViewById(R.id.btnFinish);
        btnFinish.setOnClickListener(this);
    }

    private void cancelRequest()
    {
        Map<String,Object> data=new HashMap<>();
        data.put("driverId",null);
        data.put("driverLocation",null);
        data.put("driverName",null);
        data.put("status",OrderRequestStatus.REQUESTING);
        db.collection("Requests").document(app.currentRequest.getId()).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                db.collection("Orders").document(app.currentRequest.getIdOrder()).update("trangThai","Booking").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });
    }

    private void startLocationUpdates()
    {
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        locationCallback=new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        locationClient = getFusedLocationProviderClient(this);
        locationClient.requestLocationUpdates(mLocationRequest, locationCallback,
                Looper.myLooper());
        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        if (LocationServiceTask.isLocationServiceEnabled(this)) { // 1
        } else {
            LocationServiceTask.displayEnableLocationServiceDialog(this); // 5
        }

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)

    }
    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION && grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void getLastLocation() {
        // Get last known recent location using new Google Play Services SDK (v11+)
        FusedLocationProviderClient locationClient = getFusedLocationProviderClient(this);

        if (checkPermissions()) return;

        locationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            onLocationChanged(location);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                        e.printStackTrace();
                    }
                });
    }

    private void onLocationChanged(Location location)
    {
        final GeoPoint loc = new GeoPoint(location.getLatitude(), location.getLongitude());
        db.collection("Requests").document(request.getId()).update("driverLocation",loc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                app.currentRequest.driverLocation=loc;
                placeDriverMarkerOnMap();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationClient!=null)
            locationClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.btnCancel)
        {
            cancelRequest();
        }
        else
        {
            if(id==R.id.btnFinish)
            {
                db.collection("Requests").document(request.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        db.collection("Orders").document(request.getIdOrder()).update("trangThai","Finished").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                            }
                        });
                    }
                });
            }
            else
            {
                if(id==R.id.tvStore)
                {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(request.storeLocation.getLatitude(),request.storeLocation.getLongitude()),14));
                }
                else
                {
                    if(id==R.id.tvCus)
                    {
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(request.userLocation.getLatitude(),request.userLocation.getLongitude()),14));
                    }
                }
            }
        }
    }
}

