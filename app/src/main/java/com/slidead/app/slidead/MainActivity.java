package com.slidead.app.slidead;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

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
import com.slidead.app.slidead.helpers.HttpHandler;
import com.slidead.app.slidead.helpers.JsonHelper;
import com.slidead.app.slidead.helpers.LocationHelper;
import com.slidead.app.slidead.helpers.PostClass;
import com.slidead.app.slidead.helpers.SchedulerHelper;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.slidead.app.slidead.R.id.slider;

public class MainActivity extends Activity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    ArrayList<HashMap<String, String>> urlList ;
    ArrayList<String> linkList;
    int mProgress = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        //Usage of get location address and set to location json object
//        String address = LocationHelper.getLocalityAddressString(this,"-6.8771694","107.6011578");
//        JSONObject locationObject = LocationHelper.setLocationObject("-6.8771694","107.6011578",address);

//        //Usage of scheduled job
//        scheduleJob();

//        LocationHelper loc = new LocationHelper();
//        HashMap<String,String> alt = loc.getLocation(this);
//        String latitu = "";
//        String longitu = "";
//
//        for (Map.Entry<String, String> entrySet : alt.entrySet()) {
//            String key = entrySet.getKey();
//            String value = entrySet.getValue();
//            if(key == "latitude") {
//                latitu = value;
//            }
//            if(key == "longitude") {
//                longitu = value;
//            }
//        }
//
//        Toast.makeText(this,"Send current position latitude:" + latitu + " longitude: "+ longitu, Toast.LENGTH_SHORT).show();
//
//        new PostClass(this).execute(latitu,longitu);


        JsonHelper.saveJsonLocal(this);



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
        String latitu = "";
        String longitu = "";


        for (HashMap<String, String> object: urlList) {
            String title = "";
            String imageUrl = "";
            for (Map.Entry<String, String> entrySet : object.entrySet()) {
                String key = entrySet.getKey();
                String value = entrySet.getValue();
                if(key == "url") {
                   imageUrl = value;
                }
                if(key == "title") {
                    title = value;
                }
            }
            url_maps.put(title, imageUrl);
        }

        String path = Environment.getExternalStorageDirectory().toString()+"/loocads";

        File f = new File(path);
        File files[] = f.listFiles();
        HashMap<String,File> file_maps = new HashMap<String, File>();


        for (File item : files){
            if(item.getName().contains("-1.jpg")){
                item.delete();
            }else{
                file_maps.put(item.getName(),item);
            }
        }
        // when we show slider, we must create for or while, you can add it
        for(String name : file_maps.keySet()){
            DefaultSliderView textSliderView = new DefaultSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    //.description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

//                    .setOnSliderClickListener(this);

            //add your extra information
            //textSliderView.bundle(new Bundle());
//            textSliderView.getBundle().putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }



        // you can change the animation, time page and anything.. read more on github
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setDuration(4000);
        mDemoSlider.startAutoCycle();
        mDemoSlider.setEnabled(false);
        mDemoSlider.setClickable(false);
        mDemoSlider.addOnPageChangeListener(this);





//
//        Intent refresh =new Intent(this, MainActivity.class);
//        startActivity(refresh);
//        overridePendingTransition(0,0);


    }


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
                .setTrigger(Trigger.executionWindow(0, 1))
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
//        Toast.makeText(this,"tes",Toast.LENGTH_SHORT).show();





    }

    @Override
    public void onPageSelected(int position) {


        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
