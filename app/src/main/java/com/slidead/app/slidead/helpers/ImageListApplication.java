package com.slidead.app.slidead.helpers;

import android.app.Application;
import android.os.AsyncTask;

/**
 * Created by syukur on 10/04/18.
 */

public class ImageListApplication extends Application {

    public void startService(){
        AsyncTask<String,Void, Void> imageTask = new ImageListDownloader(getApplicationContext());
        imageTask.execute();
    }
}
