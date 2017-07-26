package com.slidead.app.slidead;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.slidead.app.slidead.helpers.JsonHelper;
import com.slidead.app.slidead.helpers.SchedulerHelper;


import java.io.File;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.R.attr.value;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;


    ArrayList<HashMap<String, String>> urlList ;
    ArrayList<String> linkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //scheduleJob();

        JsonHelper.saveJsonLocal(this);


        String content = JsonHelper.readJson(this);
        // implement slider
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        // and add url image with internet
        // you can see hannibal is textview
        // and url is image
        HashMap<String,String> url_maps = new HashMap<String, String>();

        linkList = new ArrayList<String>();
        urlList =  JsonHelper.parseJson(this, content);

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

        // when we show slider, we must create for or while, you can add it
        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
//            textSliderView.getBundle().putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }

        // you can change the animation, time page and anything.. read more on github
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
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
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
