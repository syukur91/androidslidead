package com.slidead.app.slidead.helpers;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import java.util.HashMap;
import java.util.Map;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
//        Toast.makeText(context,"tes", Toast.LENGTH_SHORT).show();

        LocationHelper loc = new LocationHelper();
        HashMap<String,String> alt = loc.getLocation(context);
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

        Toast.makeText(context,"Send current position latitude:" + latitu + " longitude: "+ longitu, Toast.LENGTH_SHORT).show();

    }


}