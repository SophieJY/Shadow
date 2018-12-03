package com.example.rkuch.alpha;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private LayoutInflater mLayoutInflater;
    private ArrayList<BluetoothDevice> mDevices;
    private int mViewResourceId;

    public DeviceListAdapter(Context context, int resourceId, ArrayList<BluetoothDevice> devices) {
        super(context, resourceId, devices);
        this.mDevices = devices;
        this.mViewResourceId = resourceId;
        this.mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mLayoutInflater.inflate(mViewResourceId,null);

        BluetoothDevice device = mDevices.get(position);

        if(device != null) {
            TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
            TextView deviceAddress = (TextView) convertView.findViewById(R.id.device_address);

            if(deviceName != null) {
                deviceName.setText(device.getName());
            }
            if(deviceAddress != null) {
                deviceAddress.setText(device.getAddress());
            }
        }
        return convertView;
    }
}
