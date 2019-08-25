package com.slidead.app.slidead.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.slidead.app.slidead.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

/**
 * Created by syukur on 10/03/18.
 */

public class ImageListDownloader  extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private SpotsDialog dialog;


    public ImageListDownloader(Context context){
        mContext = context;

//        dialog = new SpotsDialog(mContext, R.style.Custom);
    }

    protected void onPreExecute() {
//        dialog.show();
        super.onPreExecute();
        Log.i(TAG, "Download begin " );
    }


    @Override
    protected Void doInBackground(String... params) {
        try {


            URL url = new URL(Constans.ENDPOINT_ADDR+"/playlists");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();

            JSONObject obj = new JSONObject();
            JSONArray array = new JSONArray();
            String deviceId = android.provider.Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            Date currentTime = Calendar.getInstance().getTime();
            ArrayList<String> imageList = new ArrayList<String>();

            File directory = new File(Environment.getExternalStorageDirectory() + "/loocads");

            if (!directory.exists()) {
                directory.mkdirs();
            }else{

                File[] files = directory.listFiles();
                for (File file : files) {
                    String fileName = file.getName();
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    array.put(fileName);
//                imageList.add(fileName);
                }

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
            String outputResult = JsonHelper.parseImageListArray(mContext,responseOutput.toString());
//            if(urlList.size() != 0){
//
//                for (HashMap<String, String> object: urlList) {
//                    String title = "";
//                    String imageUrl = "";
//                    String id = "";
//
//
//                    for (Map.Entry<String, String> entrySet : object.entrySet()) {
//                        String key = entrySet.getKey();
//                        String value = entrySet.getValue();
//                        if(key == "url") {
//                            imageUrl = value;
//                        }
//                        if(key == "title") {
//                            title = value;
//                        }
//                        if(key == "id") {
//                            id = value;
//                        }
//
//                        if(imageUrl != ""){
//                            DownloadHelper.downloadImage(imageUrl,id,mContext);
//
//                        }
//
//                    }
//
//                    Log.e(TAG, "Json parsing completed: " + title);
//                    ;
//
//                    Log.e(TAG, "Updated get image list" );
//
//                }
//
//            }





        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
//        dialog.dismiss();
    }


}
