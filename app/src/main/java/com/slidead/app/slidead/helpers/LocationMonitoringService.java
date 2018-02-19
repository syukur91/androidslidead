package com.slidead.app.slidead.helpers;

/**
 * Created by Syukur on 12/23/2017.
 */

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import com.slidead.app.slidead.LoginActivity;
import com.slidead.app.slidead.MainActivity;
import com.slidead.app.slidead.TransitionActivity;
import com.slidead.app.slidead.helpers.Constans;


/**
 * Created by devdeeds.com on 27-09-2017.
 */

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();

    Intent intents;


    public static final String ACTION_LOCATION_BROADCAST =  "LocationBroadcast";
    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    SharedPreferences.Editor editor;

    @Override
    public void onCreate() {
        super.onCreate();
        intents = new Intent(ACTION_LOCATION_BROADCAST);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(Constans.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constans.FASTEST_LOCATION_INTERVAL);
        mLocationRequest.setSmallestDisplacement(Constans.SMALLEST_DISPLACEMENT);
        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();



        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");


        if (location != null) {
            Log.d(TAG, "== location != null");

            Log.d(TAG, "Change Latitude: "+location.getLatitude()+ " Longitude: " +location.getLongitude() );


            Toast.makeText(this,"Location changed latitude:" + location.getLatitude() + " longitude: "+ location.getLongitude(), Toast.LENGTH_SHORT).show();

            AsyncTask<String,Void, Void> playlistTask =new PlaylistDownloader(this);

            playlistTask.execute(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));


//            sendMessageToUI(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));


//            editor.putString("latitude", String.valueOf(location.getLatitude()));
//            editor.putString("longitude", String.valueOf(location.getLongitude()));
//            editor.apply();

            intents.putExtra(EXTRA_LATITUDE, String.valueOf(location.getLatitude()));
            intents.putExtra(EXTRA_LONGITUDE, String.valueOf(location.getLongitude()));

           sendBroadcast(intents);
        }
    }

    private void sendMessageToUI(String lat, String lng) {

        Log.d(TAG, "Sending info...");

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intents.putExtra(EXTRA_LATITUDE, lat);
        intents.putExtra(EXTRA_LONGITUDE, lng);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intents);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }
}

