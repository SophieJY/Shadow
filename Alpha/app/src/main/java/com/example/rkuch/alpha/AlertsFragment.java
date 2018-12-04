package com.example.rkuch.alpha;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
        // TODO: compare coordinates of phone and Arduino
        isSecure = !isSecure;
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
        }
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateStatus();
        }
    };
}
