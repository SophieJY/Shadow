package com.example.rkuch.alpha;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private final static String TAG = MainActivity.class.getSimpleName();
    LocationManager locationManager;
    Location currentLocation;

    final Fragment locationFragment = new MapsFragment();
    final Fragment historyFragment = new HistoryFragment();
    final Fragment alertsFragment = new AlertsFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment activeFragment = locationFragment;

    class CoordinateInfo {
        long timeStamp;
        double latitude;
        double longitude;

        CoordinateInfo(long timeStamp, double latitude, double longitude) {
            this.timeStamp = timeStamp;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    ArrayList<CoordinateInfo> coordinateList = new ArrayList<>();
//            new Comparator<CoordinateInfo>() {
//        @Override
//        public int compare(CoordinateInfo o1, CoordinateInfo o2) {
//            if(o1.timeStamp > o2.timeStamp) {
//                return -1;
//            } else if(o1.timeStamp < o2.timeStamp) {
//                return 1;
//            } else {
//                return 0;
//            }
//        }
//    });

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_location:
                    fragmentManager.beginTransaction().hide(activeFragment).show(locationFragment).commit();
                    activeFragment = locationFragment;
                    return true;
                case R.id.navigation_history:
                    fragmentManager.beginTransaction().hide(activeFragment).show(historyFragment).commit();
                    activeFragment = historyFragment;
                    return true;
                case R.id.navigation_alerts:
                    fragmentManager.beginTransaction().hide(activeFragment).show(alertsFragment).commit();
                    activeFragment = alertsFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().add(R.id.main_container, alertsFragment, "3").hide(alertsFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, historyFragment, "2").hide(historyFragment).commit();
        fragmentManager.beginTransaction().add(R.id.main_container, locationFragment, "1").commit();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    99);
        }

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                coordinateList.clear();
                Map<String, ArrayList> value = (Map) dataSnapshot.getValue();
                DataSnapshot ds = dataSnapshot.child("struc");
                for(DataSnapshot ds_i: dataSnapshot.child("struc").getChildren()) {
                    Log.d(TAG, "onDataChange: data snapshot: " + ds_i);
                    String data = (String) ds_i.getValue();
                    String[] data_split = data.split("/");
                    coordinateList.add(new CoordinateInfo(Long.parseLong(data_split[0]), Double.parseDouble(data_split[1]), Double.parseDouble(data_split[2])));
                }
                coordinateList.sort(new Comparator<CoordinateInfo>() {
                            @Override
        public int compare(CoordinateInfo o1, CoordinateInfo o2) {
            if(o1.timeStamp > o2.timeStamp) {
                return -1;
            } else if(o1.timeStamp < o2.timeStamp) {
                return 1;
            } else {
                return 0;
            }
        }
    });

                HistoryFragment myFragment = (HistoryFragment) getSupportFragmentManager().findFragmentByTag("2");
                if(myFragment != null) {
                    myFragment.prepareEntries();
                }
                //Do this to get the sorted info
                int size = coordinateList.size();
                for(int i = 0; i < size; i++) {

                    Log.d(TAG, "onDataChange: Sort: " + coordinateList.get(i).timeStamp + " " +coordinateList.get(i).latitude + " " + coordinateList.get(i).longitude);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        // DEBUG:
        //Toast.makeText(getApplicationContext(), "ENTRY: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
