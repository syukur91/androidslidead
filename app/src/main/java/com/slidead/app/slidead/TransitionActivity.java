package com.slidead.app.slidead;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.slidead.app.slidead.helpers.JsonHelper;
import com.slidead.app.slidead.helpers.LocationHelper;
import com.slidead.app.slidead.helpers.LocationMonitoringService;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.slidead.app.slidead.R.id.slider;

public class TransitionActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String EXTRA_LATITUDE = "extra_latitude";
    public static final String EXTRA_LONGITUDE = "extra_longitude";

    public  String latitu = "";
    public  String longitu = "";
    public  Intent location_intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        LocationHelper loc = new LocationHelper();
        HashMap<String,String> alt = loc.getLocation(getApplicationContext());





        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        latitu=pref.getString("latitude", null);
        longitu=pref.getString("longitude", null);
        pref.registerOnSharedPreferenceChangeListener(this);


//        location_intent = new Intent(TransitionActivity.this, LocationMonitoringService.class);
//        startService(location_intent);

//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                new BroadcastReceiver() {
//                    @Override
//                    public void onReceive(Context context, Intent intent) {
//                        String latitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LATITUDE);
//                        String longitude = intent.getStringExtra(LocationMonitoringService.EXTRA_LONGITUDE);
//
//                        Toast.makeText(context,"EXTRA key1:" + latitude + " key2: "+ longitude, Toast.LENGTH_SHORT).show();
//
//                        if(latitude.equals(latitu) && longitude.equals(longitu) ){
//                            Toast.makeText(context.getApplicationContext(),"same", Toast.LENGTH_SHORT).show();
//                        }else{
//
//                            Intent intent_2 = new Intent(getApplicationContext(), MainActivity.class);
//                            startActivity(intent_2);
//                            finish();
////            Toast.makeText(this,"changed", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    }
//                }, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST));


//        Intent intent_2 = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent_2);
//        finish();

        Intent intent_2 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent_2);
        finish();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {

        Intent intent_2 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent_2);
        finish();
    }


}
