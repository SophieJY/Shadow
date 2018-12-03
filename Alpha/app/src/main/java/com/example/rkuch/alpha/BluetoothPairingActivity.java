package com.example.rkuch.alpha;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class BluetoothPairingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private static final String TAG = BluetoothPairingActivity.class.getSimpleName();

    BluetoothAdapter mBluetoothAdapter;
    Button btnPairing;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView mListView;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch(state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "onReceive: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "onReceive: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "onReceive: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private final BroadcastReceiver mReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);

                switch(state) {
                    //device is in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE: {
                        Log.d(TAG, "onReceive: Discoverability Enabled.");
                        break;
                    }
                    //device is not in Discoverable Mode
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE: {
                        Log.d(TAG, "onReceive: Discoverability Disabled. Able to receive connections.");
                        break;
                    }
                    case BluetoothAdapter.SCAN_MODE_NONE: {
                        Log.d(TAG, "onReceive: Discoverability Disabled. Not able to receive connections.");
                        break;
                    }
                    case BluetoothAdapter.STATE_CONNECTING: {
                        Log.d(TAG, "onReceive: Connecting...");
                        break;
                    }
                    case BluetoothAdapter.STATE_CONNECTED: {
                        Log.d(TAG, "onReceive: Connected");
                        break;
                    }
                }
            }
        }
    };

    private final BroadcastReceiver mReceiver3 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: device name: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                mListView.setAdapter(mDeviceListAdapter);
            }
        }
    };

    private final BroadcastReceiver mReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action =  intent.getAction();

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                if(mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "onReceive: BOND_BONDED");
                } else if(mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "onReceive: BOND_BONDING");
                } else if(mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "onReceive: BOND_NONE");
                }

            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called");
        super.onDestroy();
        unregisterReceiver(mReceiver);
        unregisterReceiver(mReceiver2);
        unregisterReceiver(mReceiver3);
        unregisterReceiver(mReceiver4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_pairing);

        btnPairing = findViewById(R.id.pairingButton);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mListView = findViewById(R.id.list_new_device);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver4, filter);

        mListView.setOnItemClickListener(BluetoothPairingActivity.this);

        btnPairing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: called");
                enableDisableBT();
            }
        });
    }

    private void enableDisableBT() {
        if(mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: does not have BT capability!");
        }
        if(!mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: enabling BT");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        } else if (mBluetoothAdapter.isEnabled()) {
            Log.d(TAG, "enableDisableBT: disabling BT");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        }

    }

    public void btnEnableDisableDiscoverable(View view) {
        Log.d(TAG, "btnEnableDisableDiscoverable: making device discoverable for 300 seconds");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mReceiver2, intentFilter);
    }

    public void btnDiscover(View view) {
        Log.d(TAG, "btnDiscover: looking for devices");

        if(mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: canceling discovery");
        }

        mBluetoothAdapter.startDiscovery();
        IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver3, discoverDevicesIntent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mBluetoothAdapter.cancelDiscovery();

        Log.d(TAG, "onItemClick: You clicked on a device");
        String deviceName = mBTDevices.get(position).getName();
        String deviceAddress = mBTDevices.get(position).getAddress();

        Log.d(TAG, "onItemClick: device name: " + deviceName);
        Log.d(TAG, "onItemClick: device address: " + deviceAddress);

        mBTDevices.get(position).createBond();
    }
}
