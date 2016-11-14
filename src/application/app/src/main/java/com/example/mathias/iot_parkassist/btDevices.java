package com.example.mathias.iot_parkassist;

import android.bluetooth.BluetoothDevice;

/**
 * Created by joeyd on 8-11-2016.
 */

public class btDevices {
        btDevices(String name, String mac, BluetoothDevice device){
            Name = name;
            Mac = mac;
            Device = device;
        }
    btDevices(String name, String mac){
        Name = name;
        Mac = mac;
    }
        public String Name;
        public String Mac;
        public BluetoothDevice Device;
    }


