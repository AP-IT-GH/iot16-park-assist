package com.example.mathias.iot_parkassist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class BluetoothDevicesActivity extends Activity {

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    int REQUEST_ENABLE_BT = 1;
    int  MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    IntentFilter filter = new IntentFilter();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    Button bluetoothSearchButton;
    HashMap<String, List<btDevices>> listDataChild;
    List<btDevices> pairedDevices = new ArrayList<btDevices>();
    List<btDevices> newDevices = new ArrayList<btDevices>();

//    class btDevices{
//        btDevices(String name,String mac){
//            Name = name;
//            Mac = mac;
//        }
//        public String Name;
//        public String Mac;
//    }


    private class ConnectThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device){
            BluetoothSocket tmp = null;
            mmDevice = device;
            try{
                UUID test = UUID.fromString(Installation.id(getApplicationContext()));
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString(Installation.id(getApplicationContext()).toString()));
            }catch(IOException e){

            }
            mmSocket = tmp;
        }
        public void run(){
            mBluetoothAdapter.cancelDiscovery();

            try{
                mmSocket.connect();
            }catch (IOException connectionException){
                try{
                    mmSocket.close();
                }catch (IOException closeException){
                    return;
                }
            }

//            manageCon

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);
        bluetoothSearchButton = (Button)findViewById(R.id.btSearchButton);
        bluetoothSearchButton.setOnClickListener(btnClickHandler);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mReceiver,filter);
        PairedDevice();


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        btDevices number1 = new btDevices("test1","000");
        btDevices number2 = new btDevices("test2","00");
        if(number1.equals(number2)){
            Toast.makeText(getApplicationContext(),"The same",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"fcked up",Toast.LENGTH_SHORT).show();
        }
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //first get the "category" by getting it from array listDataHeader by group position. Than get the child by child position
                btDevices Child = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                String macClickedDevice = Child.Mac;
                ConnectThread test = new ConnectThread(Child.Device);
//                test.run();
                Toast.makeText(getApplicationContext(),macClickedDevice,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        expListView.expandGroup(0);
        UUID test = UUID.fromString(Installation.id(getApplicationContext()));


        //uncomment to start searching when activity starts
//        mBluetoothAdapter.startDiscovery();
    }

    View.OnClickListener btnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.btSearchButton:
                    PairedDevice();
                    mBluetoothAdapter.startDiscovery();
                    break;

            }
        }
    };

    void askPermission(){
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(BluetoothDevicesActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(BluetoothDevicesActivity.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    void PairedDevice(){
        if(mBluetoothAdapter != null){
            if(!mBluetoothAdapter.isEnabled()){
                //bluetooth not enabled
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
            }else{
                //bluetooth enabled
                Log.e("paired devices", "Logging the paired devices");
                Set<BluetoothDevice> pairedDEvices = mBluetoothAdapter.getBondedDevices();
                if(pairedDEvices.size() > 0){
                    for (BluetoothDevice device : pairedDEvices){
                        pairedDevices.add(new btDevices(device.getName(),device.getAddress()));
                    }
                }
            }
        }else{
            //bluetooth is not supported

        }
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            askPermission();

            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                Log.e("BroadcastReceiver", "discovery starts");
                Toast.makeText(context,"discovery starts ",Toast.LENGTH_SHORT).show();
            }else if(BluetoothDevice.ACTION_FOUND.equals(action)){
                Log.e("action found","found a deivce");

                //only add if device is not already added
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(!newDevices.contains(device.getAddress()))
                {

                    Log.e("Found device", device.getName());
                    Toast.makeText(context,"Found a device"+device.getName(),Toast.LENGTH_SHORT).show();
                    newDevices.add(new btDevices(device.getName(),device.getAddress(),device));

                }


            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                mBluetoothAdapter.cancelDiscovery();
                Log.e("BroadcastReceiver", "Discovery finished");
                Toast.makeText(context,"discovery finished",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /*
     * Preparing the list data
     */


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<btDevices>>();

        // Adding child data
        listDataHeader.add("New Devices");
        listDataHeader.add("Paired Devices");

        // Adding child data

        newDevices.add(new btDevices("[Park]assistSensor"," AA-DD-FF"));
//        newDevices.add("The Godfather");
//        newDevices.add("The Godfather: Part II");
//        newDevices.add("Pulp Fiction");
//        newDevices.add("The Good, the Bad and the Ugly");
//        newDevices.add("The Dark Knight");
//        newDevices.add("12 Angry Men");


        pairedDevices.add(new btDevices("[Park]assistSensor"," AA-CC-BB"));
//        pairedDevices.add("Despicable Me 2");
//        pairedDevices.add("Turbo");
//        pairedDevices.add("Grown Ups 2");
//        pairedDevices.add("Red 2");
//        pairedDevices.add("The Wolverine");



        listDataChild.put(listDataHeader.get(0), newDevices); // Header, Child data
        listDataChild.put(listDataHeader.get(1), pairedDevices);

    }
}