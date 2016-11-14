package com.example.mathias.iot_parkassist;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;

/**
 * Created by joeyd on 8-11-2016.
 */

public class btDevices {

    public String Name;
    public String Mac;
    public BluetoothDevice Device;

    btDevices(String name, String mac, BluetoothDevice device) {
        Name = name;
        Mac = mac;
        Device = device;
    }

    btDevices(String name, String mac) {
        Name = name;
        Mac = mac;
    }

    @Override
    public int hashCode(){
        return Mac.hashCode();
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof btDevices))
            return false;
        if(obj==this)
            return true;

        btDevices other = (btDevices)obj;
        if(Mac.equals(((btDevices) obj).Mac)){
            return true;
        }
        else{
            return false;
        }

    }
}


