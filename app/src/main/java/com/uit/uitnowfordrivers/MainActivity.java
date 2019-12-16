package com.uit.uitnowfordrivers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.GeoPoint;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MainActivity extends AppCompatActivity {
    App app;
    private TabLayout tabLayout;
    Toolbar toolbar;
    TextView toolBarTextView;
    private ProfileFragment profileFragment;
    private RequestFragment requestFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolBar);
        for(int i=0;i<toolbar.getChildCount();i++)
        {
            View child=toolbar.getChildAt(i);
            if(child instanceof TextView)
            {
                toolBarTextView=(TextView)child;
                break;
            }
        }
        toolBarTextView.setText("Yêu cầu");
        // setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        createTabs();
        if(savedInstanceState==null) {
            profileFragment = new ProfileFragment();
            requestFragment = new RequestFragment();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.add(R.id.layoutContainer,requestFragment,"Request");
            ft.commit();
        }
        app=(App)getApplication();
        //  Log.e("Test",PrefUtil.loadPref(this,"email")+PrefUtil.loadPref(this,"id")+PrefUtil.loadPref(this,"name")+PrefUtil.loadPref(this,"phone"));
    }


    private void createTabs() {
        TextView tabFoodOrder = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        tabFoodOrder.setText("Yêu cầu");
        tabFoodOrder.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_order, 0, 0);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(0).setCustomView(tabFoodOrder);

        TextView tabProfile = (TextView) LayoutInflater.from(this).inflate(R.layout.layout_custom_tab, null);
        tabProfile.setText("Hồ sơ");
        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_profile, 0, 0);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.getTabAt(1).setCustomView(tabProfile);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPos = tab.getPosition();
                switch (tabPos) {
                    case 0: // Request
                        toolBarTextView.setText("Yêu cầu");
                        displayRequestFragment();
                        break;
                    case 1: // Profile
                        toolBarTextView.setText("Thông tin cá nhân");
                        displayProfileFragment();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    protected void displayRequestFragment()
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(requestFragment.isAdded())
        {
            ft.show(requestFragment);
        }
        else
        {
            ft.add(R.id.layoutContainer,requestFragment,"Request");
        }
        if(profileFragment.isAdded())
        {
            ft.hide(profileFragment);
        }
        ft.commit();
    }
    protected void displayProfileFragment()
    {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(profileFragment.isAdded())
        {
            ft.show(profileFragment);
        }
        else
        {
            ft.add(R.id.layoutContainer,profileFragment,"Profile");
        }
        if(requestFragment.isAdded())
        {
            ft.hide(requestFragment);
        }
        ft.commit();
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        if (LocationServiceTask.isLocationServiceEnabled(this)) { // 1
//            if (PermissionTask.isLocationServiceAllowed(this)) // 2
//                getLastLocation(this); // 3
//            else
//                PermissionTask.requestLocationServicePermissions(this); // 4
//        } else {
//            LocationServiceTask.displayEnableLocationServiceDialog(this); // 5
//        }
//    }
//
//    public void getLastLocation(Context context) {
//        FusedLocationProviderClient locationClient =
//                getFusedLocationProviderClient(context);
//        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if(location!=null) {
//                    //Log.e("Test", "Location Success " + String.valueOf(location.getLatitude()));
//                    onLocationChanged(location);
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("Test","Location Failed");
//                e.printStackTrace();
//            }
//        });
//    }
//    private void onLocationChanged(Location location) {
//        GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
//        app.location=geoPoint;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PermissionTask.LOCATION_SERVICE_REQUEST_CODE &&
//                grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            getLastLocation(this);
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
}
