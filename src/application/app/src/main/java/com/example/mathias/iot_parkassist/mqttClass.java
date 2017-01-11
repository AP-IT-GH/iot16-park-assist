package com.example.mathias.iot_parkassist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by mathias on 26/12/2016.
 */

public class mqttClass{
    String clientId;
    MqttAndroidClient client;

    mqttClass(Context context) {
        final Context classContext = context;
        Log.e("in mqtt", "mqqt");
        clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(classContext, "tcp://staging.thethingsnetwork.org:1883", clientId);
        //client = new MqttAndroidClient(classContext, "tcp://mqtt.luytsm.be:1883", clientId);
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                try {
                    client.connect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.e("mqqt", "message arrived");
                NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(classContext)
                        .setSmallIcon(android.R.drawable.ic_dialog_alert) //doesn't show for some reason
                        .setLargeIcon(BitmapFactory.decodeResource(classContext.getResources(), R.drawable.warning))
                        .setContentTitle("Intruder alert!")
                        .setPriority(Notification.PRIORITY_MAX)
                        //.setContentText(message.toString())
                        .setContentText("Someone has entered your property.")
                        .setVibrate(new long[] { 0, 1000, 1000, 1000, 1000 })
                        .setLights(Color.RED, 2000, 2000);

                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                mBuilder.setSound(alarmSound);

                // Sets an ID for the notification
                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) classContext.getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });

        //We will use this to connect to the things network
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("70B3D57ED00018A3");
        options.setPassword("nrmLIdzcRc+zQTpiWO7QGvwCCJtN7Kw0F5how6WjBbY=".toCharArray());

        try {
            //IMqttToken token = client.connect();
            IMqttToken token = client.connect(options); //use this one when using the password and username
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("success", "onSuccess");
                    subscribe();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("failure", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void subscribe() {
        String topic = "70B3D57ED00018A3/devices/000000009CC4931F/up";
        //String topic = "test";
        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
