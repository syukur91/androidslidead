package com.slidead.app.slidead;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.slidead.app.slidead.helpers.DownloadHelper;
import com.slidead.app.slidead.helpers.GPSTracker;
import com.slidead.app.slidead.helpers.image.ImageDownloader;

import com.slidead.app.slidead.helpers.ImageListDownloader;
import com.slidead.app.slidead.helpers.JsonHelper;
import com.slidead.app.slidead.helpers.playlist.PlaylistDownloader;

import java.io.File;

import dmax.dialog.SpotsDialog;

public class SplashActivity extends AppCompatActivity {


    private SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    34);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.CAMERA}, 1);
        }


        boolean isLatest = DownloadHelper.verifyLatestDownload(this);

        dialog = new SpotsDialog(this, R.style.Custom);

        if(!isLatest){
            AsyncTask<String,Void, Void> imageTask = new ImageDownloader(SplashActivity.this, dialog);
            imageTask.execute();
        }


        GPSTracker gps = new GPSTracker(this);
        String latitu = String.valueOf(gps.getLatitude());
        String longitu = String.valueOf(gps.getLongitude());

        Toast.makeText(this,"Send current position latitude:" + latitu + " longitude: "+ longitu, Toast.LENGTH_SHORT).show();

        editor.putString("latitude", latitu);
        editor.putString("longitude", longitu);
        editor.commit();

        AsyncTask<String,Void, Void> playlistTask =new PlaylistDownloader(SplashActivity.this);

        playlistTask.execute(latitu,longitu);


        String status =JsonHelper.getTripStatus(this);

//        Toast.makeText(this,"Trip:"+status, Toast.LENGTH_SHORT).show();

        SharedPreferences statusPref = getApplicationContext().getSharedPreferences("statusPref", MODE_PRIVATE);
        SharedPreferences.Editor statusEditor = statusPref.edit();
        statusEditor.putString("status", status);
        statusEditor.commit();


        File loocadsFolder = new File(Environment.getExternalStorageDirectory() + "/data/loocads");
        if (!loocadsFolder.exists()){
            Log.d("App", "data/loocads is exists");
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }, 8000L); //3000 L = 3 detik
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
