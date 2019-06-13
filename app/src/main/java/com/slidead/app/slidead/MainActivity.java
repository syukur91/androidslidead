package com.slidead.app.slidead;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.slidead.app.slidead.helpers.AlarmReceiver;
import com.slidead.app.slidead.helpers.GPSTracker;
import com.slidead.app.slidead.helpers.JsonHelper;
import com.slidead.app.slidead.helpers.LocationHelper;
import com.slidead.app.slidead.helpers.LocationMonitoringService;
import com.slidead.app.slidead.helpers.SchedulerHelper;
import com.slidead.app.slidead.helpers.playlist.PlaylistDownloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.slidead.app.slidead.R.id.slider;

public class MainActivity extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private CameraConfig mCameraConfig;
    private SliderLayout mDemoSlider;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    int TAKE_PHOTO_CODE = 0;
    // GPSTracker class
    private GPSTracker gps;

    private static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Code used in requesting runtime permissions.
     */
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    android.os.Handler customHandler = new android.os.Handler();

    private boolean mAlreadyStartedService = false;
    private TextView mMsgView;
    final Handler handler = new Handler();


    ArrayList<HashMap<String, String>> urlList ;
    ArrayList<String> linkList;
    int mProgress = 0;
    AlarmReceiver alarmReceiver= new AlarmReceiver();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
//        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
//
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.HOUR_OF_DAY,03);
//        calendar.set(Calendar.MINUTE, 00);
//        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);
//

        Log.d(TAG, "running a scheduler");

//        startService(new Intent(MainActivity.this, PlaylistDownloadService.class));

//        startService(new Intent(MainActivity.this, CaptureService.class));
        startService(new Intent(MainActivity.this, LocationMonitoringService.class));
        registerReceiver(broadcastReceiver, new IntentFilter(LocationMonitoringService.ACTION_LOCATION_BROADCAST));
//        registerReceiver(alarmReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));


        cleanDuplicateImage();

        GPSTracker gps = new GPSTracker(this);
        String latitu = String.valueOf(gps.getLatitude());
        String longitu = String.valueOf(gps.getLongitude());

        new PlaylistDownloader(this).execute(latitu,longitu);
        final SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String latitude=pref.getString("latitude", null);
        String longitude=pref.getString("longitude", null);
        Toast.makeText(this,"Shared preference key1:" + latitude + " key2: "+ longitude, Toast.LENGTH_SHORT).show();

        String content = JsonHelper.readJson(this);
        // implement slider
        mDemoSlider = (SliderLayout)findViewById(slider);
        // and add url image with internet
        // you can see hannibal is textview
        // and url is image
        HashMap<String,String> url_maps = new HashMap<String, String>();

        linkList = new ArrayList<String>();
        urlList =  JsonHelper.parseJson(this, content);

        LocationHelper loc = new LocationHelper();
        HashMap<String,String> alt = loc.getLocation(this);

        SharedPreferences statusPref = getApplicationContext().getSharedPreferences("statusPref", MODE_PRIVATE);
        String status=statusPref.getString("status", null);

//        Toast.makeText(this,"Trip: "+status, Toast.LENGTH_SHORT).show();


//        handler.postDelayed(runnable, 5000);




    }

    private Runnable updateTimerThread = new Runnable()
    {
        public void run()
        {

            Toast.makeText(MainActivity.this,"Shared preference key1:", Toast.LENGTH_SHORT).show();
            ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
            timer.scheduleAtFixedRate(updateTimerThread, 5, 5, TimeUnit.SECONDS);
        }
    };

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            //Do your refreshing
//            GPSTracker gps = new GPSTracker(MainActivity.this);
//            String latitu = String.valueOf(gps.getLatitude());
//            String longitu = String.valueOf(gps.getLongitude());
//
//            new PlaylistDownloader(MainActivity.this).execute(latitu,longitu);
//            //This basically reruns this runnable in 5 seconds
//            handler.postDelayed(this, 20000);
//
////            finish();
////            overridePendingTransition(0, 0);
////            startActivity(getIntent());
////            overridePendingTransition(0, 0);
//
//
//            Intent refresh = new Intent(MainActivity.this, MainActivity.class);
//            startActivity(refresh);
//            MainActivity.this.finish(); //
//        }
//    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            try {

            String result = "";

            HashMap<String,File> file_maps = new HashMap<String, File>();

            file_maps.clear();
            mDemoSlider.removeAllSliders();

            String path = Environment.getExternalStorageDirectory().toString()+"/data/loocads";

            File f = new File(context.getExternalFilesDir(null), "/data/loocads");
            File files[] = f.listFiles();

            File testFile = new File(context.getExternalFilesDir(null), "TestFile.txt");

            if (!testFile.exists())
                testFile.createNewFile();

            SharedPreferences statusPref = getApplicationContext().getSharedPreferences("statusPref", MODE_PRIVATE);
            String status=statusPref.getString("status", null);

//            if(status.equals("start")){

                if(testFile.exists()){

                    String content = JsonHelper.readJson(context);
                    mDemoSlider = (SliderLayout)findViewById(slider);
                    HashMap<String,String> url_maps = new HashMap<String, String>();
                    linkList = new ArrayList<String>();
                    urlList =  JsonHelper.parseJson(context, content);

                    for (HashMap<String, String> object: urlList) {
                        String title = "";
                        String name = "";
                        for (Map.Entry<String, String> entrySet : object.entrySet()) {
                            String key = entrySet.getKey();
                            String value = entrySet.getValue();
                            if(key == "id") {
                                name = value;
                            }
                            if(key == "title") {
                                title = value;
                            }
                        }
                        url_maps.put(title, name);
                    }



                    for (File item : files){
                        if(item.getName().contains("-1.jpg")){
                            item.delete();
                        }else{
                            file_maps.put(item.getName(),item);
                        }
                    }

                    // when we show slider, we must create for or while, you can add it
                    for(String name : url_maps.keySet()){
                        DefaultSliderView textSliderView = new DefaultSliderView(MainActivity.this);
                        File file = new File(context.getExternalFilesDir(null), "/data/loocads/" + url_maps.get(name)+".jpg");
                        // initialize a SliderLayout
                        textSliderView.image(file).setScaleType(BaseSliderView.ScaleType.Fit);
                        textSliderView.description(file.getName());
                        result += url_maps.get(name)+",";
                        mDemoSlider.addSlider(textSliderView);

                    }


                    // you can change the animation, time page and anything.. read more on github
                    mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                    mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                    mDemoSlider.setDuration(4000);
                    mDemoSlider.startAutoCycle();
                    mDemoSlider.setEnabled(false);
                    mDemoSlider.setClickable(false);
                    mDemoSlider.addOnPageChangeListener(MainActivity.this);
                }


//            }

            } catch (IOException e) {
                Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");

            }

        }
    };

    private void scheduleJob() {
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key", "some_value");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Job myJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(SchedulerHelper.class)
                // uniquely identifies the job
                .setTag("discover-sdk-tag")
                // one-off job
                .setRecurring(true)
                // don't persist past a device reboot
                .setLifetime(Lifetime.FOREVER)
                // start between 0 and 90 seconds from now
                .setTrigger(Trigger.executionWindow(0, 10))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_ANY_NETWORK
                        // only run when the device is charging
                )
                .setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }

    private void cleanDuplicateImage(){

//        String path = Environment.getExternalStorageDirectory().toString()+"/loocads";
        File f = new File(this.getApplicationContext().getExternalFilesDir(null), "/data/loocads");
        File files[] = f.listFiles();

        for (File item : files){
            if(item.getName().toString().contains("-1.jpg")){
                item.delete();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
        }
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onStart() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.startAutoCycle();
        super.onStart();
    }



    @Override
    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();

        // you can handle even this method
        // webview or go activity
        // ok dude
        // thanks for watching
        // if you have ask, please comments like and subsribe
        // thanks
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
//
//        if (state == 0 ) {
////
////                String desc =mDemoSlider.getCurrentSlider().getDescription();
////                Log.d("Slider Demo", "File Name: " + desc);
////                Log.d("Slider Demo", "State: " + state);
////
////                String played = JsonHelper.saveImagePlayed(MainActivity.this,desc);
////              Log.d("Slider Demo", mDemoSlider.getCurrentSlider().toString());
////
//        }

    }


    @Override
    protected void onResume() {
//        IntentFilter smsFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//        registerReceiver(alarmReceiver,smsFilter);
        super.onResume();
        // pause code
    }


    @Override
    protected void onPause() {
//        unregisterReceiver(alarmReceiver);
        unregisterReceiver(broadcastReceiver);
        super.onPause();
        // pause code
    }




}





