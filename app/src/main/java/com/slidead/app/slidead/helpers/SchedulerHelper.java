package com.slidead.app.slidead.helpers;

/**
 * Created by Syukur on 7/22/2017.
 * Scheduler to download API list
 */
import android.content.Context;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.slidead.app.slidead.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SchedulerHelper extends JobService {


    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work here
        Log.d("Service", "onStartJob ");
        Toast.makeText(this, R.string.job_started, Toast.LENGTH_LONG).show();
//        final String url = "http://cdn3.nflximg.net/images/3093/2043093.jpg";
//        DownloadHelper.downloadImage(url,this);

        LocationHelper loc = new LocationHelper();
        HashMap<String,String> alt = loc.getLocation(this);
        String latitu = "";
        String longitu = "";

        for (Map.Entry<String, String> entrySet : alt.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            if(key == "latitude") {
                latitu = value;
            }
            if(key == "longitude") {
                longitu = value;
            }
        }

        Toast.makeText(this,"Send current position latitude:" + latitu + " longitude: "+ longitu, Toast.LENGTH_SHORT).show();


//        int clock = Integer.parseInt(hour);
//        if(clock == 3 || clock >= 3){
//            UrlGetterHelper parser = new UrlGetterHelper(this);
//            parser.execute();
//        }
        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Toast.makeText(this, R.string.job_finished, Toast.LENGTH_LONG).show();
        return true; // Answers the question: "Should this job be retried?"
    }


    public static void scheduleJob(Context c) {
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key", "some_value");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(c));

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
                .setTrigger(Trigger.executionWindow(0, 0))
                // don't overwrite an existing job with the same tag
                .setReplaceCurrent(false)
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                // constraints that need to be satisfied for the job to run
                .setConstraints(
                        // only run on an unmetered network
                        Constraint.ON_UNMETERED_NETWORK

                )
                .setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }


}
