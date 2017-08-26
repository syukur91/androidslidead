package com.slidead.app.slidead.helpers;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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

    public static String getCompleteAddressString(Context ctx ,String LATITUDE, String LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(LATITUDE), Double.parseDouble(LONGITUDE), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(" ");
                }
                strAdd = strReturnedAddress.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static JSONObject setLocationObject(String LATITUDE, String LONGITUDE, String ADRESSLINE){

        JSONObject location = new JSONObject();
        try {
            location.put("latitude", LATITUDE);
            location.put("longitude", LONGITUDE);
            location.put("address", ADRESSLINE);
            return location;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

    }

    public static String getLocalityAddressString(Context ctx ,String LATITUDE, String LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(LATITUDE), Double.parseDouble(LONGITUDE), 1);
            if (addresses != null) {
                String locality = addresses.get(0).getLocality();
                strAdd = locality.toString();
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

}
