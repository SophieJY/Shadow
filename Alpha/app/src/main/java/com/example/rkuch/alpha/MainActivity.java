package com.example.rkuch.alpha;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    //final Fragment locationFragment = new LocationFragment();
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
    }

}
