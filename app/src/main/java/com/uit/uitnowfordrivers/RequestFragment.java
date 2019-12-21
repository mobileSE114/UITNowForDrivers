package com.uit.uitnowfordrivers;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class RequestFragment extends Fragment implements RequestAdapter.RequestListener {
    RecyclerView rvRequests;
    RequestAdapter adapter;
    SwipeRefreshLayout swipeRequests;
    FirebaseFirestore db;
    App app;
    ArrayList<Request> requests = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.request_fragment, container, false);
        rvRequests = view.findViewById(R.id.rvRequests);
        swipeRequests = view.findViewById(R.id.swipeRequests);
        swipeRequests.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRequests();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        app = (App) getActivity().getApplication();
        getRequests();
        //   getActivity().getActionBar().setTitle("Đơn Hàng");
    }

    private void getRequests() {
        requests.clear();
        db.collection("Requests").whereEqualTo("status", OrderRequestStatus.REQUESTING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Request r = document.toObject(Request.class);
                        Log.e("Test", "Order " + r.getId());
                        requests.add(r);
                    }
                    adapter = new RequestAdapter(requests, RequestFragment.this);
                    rvRequests.setAdapter(adapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    rvRequests.setLayoutManager(layoutManager);
	            swipeRequests.setRefreshing(false);
                } else {
                    Log.e("Test", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    @Override
    public void onAcceptRequest(final Request request) {
     //   Toast.makeText(getActivity(),app.location.getLatitude()+" "+app.location.getLongitude(),Toast.LENGTH_SHORT).show();
        if (app.location == null) {
            Toast.makeText(getActivity(), "Không thể xác định vị trí của bạn", Toast.LENGTH_SHORT).show();
        } else {
            app.currentRequest=request;
            app.currentRequest.setDriverLocation(app.location);
            app.currentRequest.setDriverName(app.driver.getName());
            app.currentRequest.setDriverId(app.driver.getId());
            app.currentRequest.setDriverPhone(app.driver.getPhone());
            Map<String, Object> data = new HashMap<>();
            data.put("driverId", app.driver.getId());
            data.put("driverName", app.driver.getName());
            data.put("driverLocation", app.location);
            data.put("status",OrderRequestStatus.ACCEPTED);
            data.put("driverPhone",app.driver.getPhone());
            db.collection("Requests").document(request.getId()).set(data, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(getActivity(), RequestTrackingActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LocationServiceTask.isLocationServiceEnabled(getActivity())) { // 1
            if (PermissionTask.isLocationServiceAllowed(getActivity())) // 2
            {
                Toast.makeText(getActivity(), "Đang lấy vị trí...", Toast.LENGTH_SHORT).show();
                getLastLocation(getActivity()); // 3
            } else
                PermissionTask.requestLocationServicePermissions(getActivity()); // 4
        } else {
            LocationServiceTask.displayEnableLocationServiceDialog(getActivity()); // 5
        }
        getRequests();
    }

    private void getLastLocation(Context context) {
        FusedLocationProviderClient locationClient =
                getFusedLocationProviderClient(context);
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.e("Test", "Location Success " + String.valueOf(location.getLatitude()));
                    onLocationChanged(location);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Test", "Location Failed");
                e.printStackTrace();
            }
        });
    }

    private void onLocationChanged(Location location) {
        GeoPoint geoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        app.location = geoPoint;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionTask.LOCATION_SERVICE_REQUEST_CODE &&
                grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation(getActivity());
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
