package com.example.mathias.iot_parkassist;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * Created by mathias on 26/12/2016.
 */

public class bluetooth extends MainActivity{
    
    int REQUEST_ENABLE_BT = 10;
    //Activity activity;
    public BluetoothAdapter bluetoothAdapter;
    public Set<BluetoothDevice> pairedDevices;

    bluetooth(Activity activity) {
        //this.activity = activity;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
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
        getPairedDevices();
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

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String read(BluetoothDevice device, InputStream input) {
        //getPairedDevices();
        String readMessage= null;
        //if (!pairedDevices.isEmpty()) {
        if (device != null) {
            //BluetoothConnector bc = new BluetoothConnector(device, true, bluetoothAdapter, null);
            //Log.e(TAG, pairedDevices.iterator().next().toString());
            try {
                //BluetoothConnector.BluetoothSocketWrapper bs = bc.connect();
                byte[] bytes = new byte[1024];
                //Log.e("bytes", bytes.toString());
                while(true) {
                    //InputStream input = bs.getInputStream();
                    //Log.e("available", String.valueOf(input.available()));
                    if (input.available() > 0) {
                        //Log.e("available", String.valueOf(input.available()));
                        int byteAmount = input.read(bytes);
                        Log.e("bytes", bytes.toString());
                        Log.e("byteAmount", String.valueOf(byteAmount));
                        readMessage = new String(bytes, 0, byteAmount);
                        Log.e("message", readMessage);
                        break;
                        //bs.close();
                    } else SystemClock.sleep(300);
                }
                //bs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return readMessage;
    }

    public void getPairedDevices() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        Log.e("pairedDevices", pairedDevices.toString());
    }

}





