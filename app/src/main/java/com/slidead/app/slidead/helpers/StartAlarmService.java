package com.slidead.app.slidead.helpers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

/**
 * Created by syukur on 16/03/18.
 */

public class StartAlarmService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AsyncTask<String,Void, Void> imageTask = new ImageListDownloader(getApplicationContext());
        imageTask.execute();

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
        }

@Override
public IBinder onBind(Intent intent) {
        return null;
        }


}
