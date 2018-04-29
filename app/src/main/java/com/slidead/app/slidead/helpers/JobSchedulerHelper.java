package com.slidead.app.slidead.helpers;

/**
 * Created by syukur on 25/02/18.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.slidead.app.slidead.MainActivity;
import com.slidead.app.slidead.R;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Rajesh Pattanaik
 */




public class JobSchedulerHelper extends Job {

    public  static final String TAG = "show_job_tag";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
//        PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
//                new Intent(getContext(), MainActivity.class), 0);
//
//        Notification notification = new NotificationCompat.Builder(getContext())
//                .setContentTitle("Android Job Demo")
//                .setContentText("Notification from Android Job Demo App.")
//                .setAutoCancel(true)
//                .setContentIntent(pi)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setShowWhen(true)
//                .setColor(Color.RED)
//                .setLocalOnly(true)
//                .build();
//
//        NotificationManagerCompat.from(getContext())
//                .notify(new Random().nextInt(), notification);

//        Toast.makeText(getContext(),"running a schedule", Toast.LENGTH_SHORT).show();


        Log.d(TAG, "running a scheduler");


        return Result.SUCCESS;
    }
    public static void schedulePeriodic() {

        new JobRequest.Builder(JobSchedulerHelper.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(5), TimeUnit.MINUTES.toMillis(2))
//                .setExecutionWindow(1000, 2000)
//                .setPeriodic(5000)
                .setUpdateCurrent(true)
                .setPersisted(true)
                .build()
                .schedule();
    }


}