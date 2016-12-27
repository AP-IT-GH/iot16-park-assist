package com.example.mathias.iot_parkassist;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;


/**
 * Created by mathias on 26/12/2016.
 */

public class bluetooth extends MainActivity{
    
    int REQUEST_ENABLE_BT = 10;
    Activity activity;
    BluetoothAdapter bluetoothAdapter;
    private Set<BluetoothDevice> pairedDevices;

    bluetooth(Activity activity) {
        this.activity = activity;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            // Device does not support Bluetooth
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }





            /*bluetoothAdapter.startDiscovery();
            // Register for broadcasts when a device is discovered.
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            getApplicationContext().registerReceiver(mReceiver, filter);*/
        }
    }

    public void send(String message) {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        Log.e("pairedDevices", pairedDevices.toString() );
        if (!pairedDevices.isEmpty()) {
            //new ConnectThread(pairedDevices.iterator().next()).run();
            BluetoothConnector bc = new BluetoothConnector(pairedDevices.iterator().next(), true, bluetoothAdapter, null);
            try {
                BluetoothConnector.BluetoothSocketWrapper bs = bc.connect();
                Log.e(TAG, bs.getRemoteDeviceName());
                Log.e(TAG, message.getBytes().toString());
                bs.getOutputStream().write(message.getBytes());
                Log.e(TAG, "write success");
                bs.close();
                    /*byte[] buffer = new byte[1024];
                    int bytes;
                    // Keep looping to listen for received messages
                    while (true) {
                        try {
                            bytes = bs.getInputStream().read(buffer);            //read bytes from input buffer
                            String readMessage = new String(buffer, 0, bytes);
                            Log.e(TAG, readMessage);
                        } catch (IOException e) {
                            break;
                        }
                    }*/

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // Create a BroadcastReceiver for ACTION_FOUND.
    /*private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "in receiver");
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.e("deviceName=  ", deviceName);
                Log.e("deviceHardwareAdress= ", deviceHardwareAddress);
            }
        }
    };*/

    /*@Override
    protected void onDestroy() {
        super.onDestroy();

        //unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }*/

    /*private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;
            Log.e(TAG, "in connectThread");
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createInsecureRfcommSocketToServiceRecord((UUID.fromString("15fa13d2-0f5c-4532-95ab-eeebb57baf4d")));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e(TAG, "Socket connection success");
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                Log.e(TAG, connectException.toString());

                try {
                    mmSocket.close();
                    Log.e(TAG, "Socket connection failure");
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            //manageMyConnectedSocket(mmSocket);
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }*/
}





