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
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LocationManager locationManager;
    Location currentLocation;

    final Fragment locationFragment = new MapsFragment();
    final Fragment historyFragment = new HistoryFragment();
    final Fragment alertsFragment = new AlertsFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment activeFragment = locationFragment;

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
