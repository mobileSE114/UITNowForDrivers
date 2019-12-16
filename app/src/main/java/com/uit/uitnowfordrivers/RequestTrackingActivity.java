package com.uit.uitnowfordrivers;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RequestTrackingActivity extends AppCompatActivity implements OnMapReadyCallback {
    RelativeLayout layoutRequesting, layoutInfor;
    App app;
    GoogleMap map;
    TextView tvDelivery, tvStore, tvTotal;
    Button btnCancelRequest;
    FirebaseFirestore db;
    GeoPoint store,delivery,driver;
    Request request;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        placeUserMarkerOnMap();
        placeStoreMarkerOnMap();
        placeDriverMarkerOnMap();
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(request.storeLocation.getLatitude(),request.storeLocation.getLongitude()),16));
    }

    private void placeDriverMarkerOnMap() {
        MarkerOptions options = new MarkerOptions().position(new LatLng(request.driverLocation.getLatitude(), request.driverLocation.getLongitude())).title(app.driver.getName());
        options.icon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_motor)));
        map.addMarker(options);
     //   map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(driver.getLatitude(),driver.getLongitude()),18));
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
        btnCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest();
            }
        });
        tvDelivery.setText(request.getUserAddress());
        tvStore.setText(request.getStoreAddress());
        tvTotal.setText(request.getTotal());
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
                db.collection("Orders").document(app.currentRequest.getOrderId()).update("trangThai","Booking").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
            }
        });
    }

}
