package com.example.rkuch.alpha;

import android.location.Location;
import android.location.LocationListener;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Timer;
import java.util.TimerTask;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapsFragment.class.getSimpleName();
    private MapView mapView;
    private GoogleMap mMap;
    private Location currentLocation;
    private Timer timer = new Timer();
    private boolean hasNotYetUpdated = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        mapView = view.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(runnable);
            }
        }, 0, 5000);
    }

    private void updateMap() {
        MainActivity activity = (MainActivity) getActivity();
        currentLocation = activity.currentLocation;
        if (currentLocation == null) {
            return;
        }
        mMap.clear();
        LatLng startLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        Log.d(TAG, "onMapReady: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(startLocation).title("You"));

        // Only move the camera the first time to prevent sharp jumps
        if (hasNotYetUpdated) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15));
            hasNotYetUpdated = false;
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateMap();
        }
    };
}
