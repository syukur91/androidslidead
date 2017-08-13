package com.slidead.app.slidead.helpers;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import java.util.HashMap;

import im.delight.android.location.SimpleLocation;

/**
 * Created by Syukur on 8/13/2017.
 */

public class LocationHelper extends Activity {

    public  HashMap<String, String> getLocation(Context ctx){

        SimpleLocation location;
        String latitude;
        String longitude;

        location = new SimpleLocation(ctx,true);
        location.beginUpdates();
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(ctx);
        }

        HashMap<String, String> list = new HashMap<>();
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());

        list.put("longitude", longitude);
        list.put("latitude",latitude);

        return list;
    }

}
