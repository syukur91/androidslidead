package com.slidead.app.slidead.helpers;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by syukur on 14/04/18.
 */

public class ImageListDownloaderService extends Service {

    private static String TAG = ImageListDownloaderService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        mythread  = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }

    public void readWebPage(){

        try {
            URL url = new URL(Constans.ENDPOINT_ADDR+"/playlists");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();


            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            String deviceId = android.provider.Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Date currentTime = Calendar.getInstance().getTime();
            ArrayList<String> imageList = new ArrayList<String>();

            File directory = new File(Environment.getExternalStorageDirectory() + "/loocads");
            File[] files = directory.listFiles();
            for (File file : files) {
                String fileName = file.getName();
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
                array.put(fileName);
//                imageList.add(fileName);
            }

            if (!directory.exists()) {
                throw new IOException("Folder not found");
            }

            obj.put("deviceId", "MHZ87999");
            obj.put("time", currentTime);
            obj.put("imageList", array);


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(obj.toString());
            wr.flush();
            wr.close();

            final StringBuilder output = new StringBuilder("Request URL " + url);
            output.append(System.getProperty("line.separator")  + "Type " + "POST");
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            ArrayList<HashMap<String, String>> urlList ;
            urlList = null;
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);

            }
            br.close();

            output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
            urlList =  JsonHelper.parseImageListResponse(responseOutput.toString());
            String outputResult = JsonHelper.parseImageListArray(this.getApplicationContext(),responseOutput.toString());
            if(urlList.size() != 0){

                for (HashMap<String, String> object: urlList) {
                    String title = "";
                    String imageUrl = "";
                    String id = "";


                    for (Map.Entry<String, String> entrySet : object.entrySet()) {
                        String key = entrySet.getKey();
                        String value = entrySet.getValue();
                        if(key == "url") {
                            imageUrl = value;
                        }
                        if(key == "title") {
                            title = value;
                        }
                        if(key == "id") {
                            id = value;
                        }

                        if(imageUrl != ""){
                            DownloadHelper.downloadImage(imageUrl,id,this.getApplicationContext());

                        }

                    }

                    Log.e(TAG, "Json parsing completed: " + title);
                    ;

                    Log.e(TAG, "Updated get image list" );

                }

            }

            Log.e(TAG, "Json parsing completed: " + responseOutput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MyThread extends Thread{
        static final long DELAY = 3000;
        @Override
        public void run(){

            readWebPage();
//            while(isRunning){
//                Log.d(TAG,"Running");
//                try {
//                    readWebPage();
//                    Thread.sleep(DELAY);
//                } catch (InterruptedException e) {
//                    isRunning = false;
//                    e.printStackTrace();
//                }
//            }
        }

    }


}
