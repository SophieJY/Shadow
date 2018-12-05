package com.example.rkuch.alpha;


import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlertsFragment extends Fragment {
    private boolean isSecure = true;
    private Timer timer = new Timer();
    private final static double RANGE = .00045;

    public AlertsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alerts, container, false);
        view.setBackgroundColor(Color.GRAY);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(runnable);
            }
        }, 0, 5000);
        return view;
    }

    private void updateStatus() {
        MainActivity activity = (MainActivity)getActivity();
        Location phoneLocation = activity.currentLocation;
        if (phoneLocation == null || activity.coordinateList.isEmpty()) {
            return;
        }
        MainActivity.CoordinateInfo trackerLocation = activity.coordinateList.get(0);
        isSecure = isWithinRange(
                phoneLocation.getLatitude(), phoneLocation.getLongitude(),
                trackerLocation.latitude, trackerLocation.longitude, RANGE);
        View view = getView().findViewById(R.id.alerts_frame_layout);
        TextView textView = getView().findViewById(R.id.alerts_text_view);
        ImageView imageView = getView().findViewById(R.id.alerts_image_view);

        if (isSecure) {
            view.setBackgroundColor(Color.GREEN);
            textView.setText("Your device is secure.");
            imageView.setImageResource(R.drawable.ic_secure_64dp);
        } else {
            view.setBackgroundColor(Color.RED);
            textView.setText("Your device is not secure. Check map.");
            imageView.setImageResource(R.drawable.ic_warning_64dp);

            ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
            toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,2000);

            if (Build.VERSION.SDK_INT >= 26) {
                activity.vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                activity.vibrator.vibrate(1000);
            }
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateStatus();
        }
    };

    private boolean isWithinRange(double latA, double lonA, double latB, double lonB, double range) {
        double latDiff = latA - latB;
        double lonDiff = lonA - lonB;
        double distance = Math.sqrt(Math.pow(latDiff, 2) + Math.pow(lonDiff, 2));
        return distance <= range;
    }
}
