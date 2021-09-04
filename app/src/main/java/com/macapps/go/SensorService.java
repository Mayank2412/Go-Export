package com.macapps.go;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GestureDetectorCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SensorService extends Service {
    private SensorManager mSensorManager;

    private Sensor mAccelerometer;
    private com.macapps.go.ShakeDetector mShakeDetector;

    public SensorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//region PRE-FUNCTIONS-2

        SharedPreferences newPref = getSharedPreferences("key2", Context.MODE_PRIVATE);
//endregion
        //start the foreground service

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground();
        else
            startForeground(1, new Notification());

        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();


        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @SuppressLint("MissingPermission")
            @Override
            public void onShake(int count) {
                //check if the user has shaken the phone for 3 time in a row
                if (count == 3) {
                    String phonenumber1 = newPref.getString("phonenumber1", "");
                    String phonenumber2 = newPref.getString("phonenumber2", "");
                    String phonenumber3 = newPref.getString("phonenumber3", "");
                    String phonenumber4 = newPref.getString("phonenumber4", "");
                    String phonenumber5 = newPref.getString("phonenumber5", "");
                    String phonenumber6 = newPref.getString("phonenumber6", "");
                    String phonenumber7 = newPref.getString("phonenumber7", "");
                    String phonenumber8 = newPref.getString("phonenumber8", "");
                    String phonenumber9 = newPref.getString("phonenumber9", "");
                    String phonenumber10 = newPref.getString("phonenumber10", "");
                    //vibrate the phone
                    vibrate();
                    BatteryManager bm = (BatteryManager) getSystemService(Service.BATTERY_SERVICE);
                    Double batteryPct = (double) bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    Integer chargeCounter = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CHARGE_COUNTER);
                    Integer capacity = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
                    //create FusedLocationProviderClient to get the user location
                    FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                    //use the PRIORITY_BALANCED_POWER_ACCURACY so that the service doesn't use unnecessary power via GPS
                    //it will only use GPS at this very moment
                    fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, new CancellationToken() {
                        @Override
                        public boolean isCancellationRequested() {
                            return false;
                        }

                        @NonNull
                        @Override
                        public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            //check if location is null
                            //for both the cases we will create different messages
                            if (location != null) {

                                //get the SMSManager
                                SmsManager smsManager = SmsManager.getDefault();
                                //get the list of all the contacts in Database
                                ArrayList<String> list = new ArrayList<String>();
                                list.add(phonenumber1);
                                list.add(phonenumber2);
                                list.add(phonenumber3);
                                list.add(phonenumber4);
                                list.add(phonenumber5);
                                list.add(phonenumber6);
                                list.add(phonenumber7);
                                list.add(phonenumber8);
                                list.add(phonenumber9);
                                list.add(phonenumber10);
                                //send SMS to each contact
                                for (int i = 0; i < list.size(); i++) {
                                    String message = "Hey! I am in DANGER! Reach me out. My coordinates.\n " + "http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude() + "\n This is my battery percentage: " + batteryPct.toString() + "%";
                                    if (!list.get(i).equals(""))
                                        smsManager.sendTextMessage(
                                                list.get(i),
                                                null,
                                                message,
                                                null,
                                                null
                                        );
                                }

                            } else {
                                String message = "I am in DANGER! I need help. Call your nearest Police Station.\n This is my battery percentage: \n" + batteryPct.toString() + "%";
                                SmsManager smsManager = SmsManager.getDefault();
                                ArrayList<String> list = new ArrayList<String>();
                                list.add(phonenumber1);
                                list.add(phonenumber2);
                                list.add(phonenumber3);
                                list.add(phonenumber4);
                                list.add(phonenumber5);
                                list.add(phonenumber6);
                                list.add(phonenumber7);
                                list.add(phonenumber8);
                                list.add(phonenumber9);
                                list.add(phonenumber10);
                                //send SMS to each contact
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i) != "")
                                        smsManager.sendTextMessage(
                                                list.get(i),
                                                null,
                                                message,
                                                null,
                                                null
                                        );
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Check: ", "OnFailure");
                            String message = "I am in DANGER! I need help. Call your nearest Police Station.\n This is my battery percentage: \n" + batteryPct.toString() + "%";
                            SmsManager smsManager = SmsManager.getDefault();
                            ArrayList<String> list = new ArrayList<String>();
                            list.add(phonenumber1);
                            list.add(phonenumber2);
                            list.add(phonenumber3);
                            list.add(phonenumber4);
                            list.add(phonenumber5);
                            list.add(phonenumber6);
                            list.add(phonenumber7);
                            list.add(phonenumber8);
                            list.add(phonenumber9);
                            list.add(phonenumber10);
                            //send SMS to each contact
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i) != "")
                                    smsManager.sendTextMessage(
                                            list.get(i),
                                            null,
                                            message,
                                            null,
                                            null
                                    );
                            }
                        }
                    });

                }
            }
        });

        //register the listener
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

    }

    public void vibrate() {
        final Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect vibEff;
        //Android Q and above have some predefined vibrating patterns
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibEff = VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK);
            vibrator.cancel();
            vibrator.vibrate(vibEff);
        } else {
            vibrator.vibrate(500);
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_MIN);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("GO is active.")
                .setContentText("Shake your device thrice to send a message.")

                //this is important, otherwise the notification will show the way
                //you want i.e. it will show some default notification
                .setSmallIcon(R.mipmap.ic_symbol_new_foreground)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    @Override
    public void onDestroy() {

        //create an Intent to call the Broadcast receiver
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, ReactivateService.class);
        this.sendBroadcast(broadcastIntent);
        super.onDestroy();
    }


}