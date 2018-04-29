package com.slidead.app.slidead.helpers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.slidead.app.slidead.MainActivity;
import com.slidead.app.slidead.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
//        Toast.makeText(context,"tes", Toast.LENGTH_SHORT).show();
        final PendingResult pendingResult = goAsync();


//        AsyncTask<String, Integer, String> asyncTask = new AsyncTask<String, Integer, String>() {
//            @Override
//            protected String doInBackground(String... params) {
//
//                try {
//                java.net.URL url = new java.net.URL("http://45.76.178.16:4443/playlists");
//                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
//                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
//                httpURLConnection.connect();
//
//                    JSONObject obj = new JSONObject();
//                    JSONArray array = new JSONArray();
//                    String deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
//                    Date currentTime = Calendar.getInstance().getTime();
//                    ArrayList<String> imageList = new ArrayList<String>();
//
//                    File directory = new File(Environment.getExternalStorageDirectory() + "/loocads");
//                    File[] files = directory.listFiles();
//                    for (File file : files) {
//                        String fileName = file.getName();
//                        fileName = fileName.substring(0, fileName.lastIndexOf("."));
//                        array.put(fileName);
////                imageList.add(fileName);
//                    }
//
//                    if (!directory.exists()) {
//                        throw new IOException("Folder not found");
//                    }
//
//                    obj.put("deviceId", "MHZ87999");
//                    obj.put("time", currentTime);
//                    obj.put("imageList", array);
//
//
//                    DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
//                    wr.writeBytes(obj.toString());
//                    wr.flush();
//                    wr.close();
//
//                    final StringBuilder output = new StringBuilder("Request URL " + url);
//                    output.append(System.getProperty("line.separator")  + "Type " + "POST");
//                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                    String line = "";
//                    StringBuilder responseOutput = new StringBuilder();
//                    System.out.println("output===============" + br);
//                    ArrayList<HashMap<String, String>> urlList ;
//                    urlList = null;
//                    while((line = br.readLine()) != null ) {
//                        responseOutput.append(line);
//
//                    }
//                    br.close();
//
//                    output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
//                    urlList =  JsonHelper.parseImageListResponse(responseOutput.toString());
//                    String outputResult = JsonHelper.parseImageListArray(context,responseOutput.toString());
//                    if(urlList.size() != 0){
//
//                        for (HashMap<String, String> object: urlList) {
//                            String title = "";
//                            String imageUrl = "";
//                            String id = "";
//
//
//                            for (Map.Entry<String, String> entrySet : object.entrySet()) {
//                                String key = entrySet.getKey();
//                                String value = entrySet.getValue();
//                                if(key == "url") {
//                                    imageUrl = value;
//                                }
//                                if(key == "title") {
//                                    title = value;
//                                }
//                                if(key == "id") {
//                                    id = value;
//                                }
//
//                                if(imageUrl != ""){
//                                    DownloadHelper.downloadImage(imageUrl,id,context);
//
//                                }
//
//                            }
//
//                            Log.e(TAG, "Json parsing completed: " + title);
//                            ;
//
//                            Log.e(TAG, "Updated get image list" );
//
//                        }
//
//                    }
//
//                    Log.e(TAG, "Json parsing completed: " + responseOutput);
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                pendingResult.finish();
//             return null;
//            }
//        };

//        asyncTask.execute();


        try{
//            AsyncTask<String,Void, Void> imageTask = new ImageListDownloader(context);
//            pendingResult.finish();
//            imageTask.execute();

            context.startService(new Intent(context.getApplicationContext(), ImageListDownloaderService.class));


        }catch(Exception e){


        }


    }





}