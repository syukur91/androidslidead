package com.slidead.app.slidead.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.slidead.app.slidead.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

/**
 * Created by syukur on 28/01/18.
 */

public class ImageDownloader extends AsyncTask<String, Void, Void> {


    private Context mContext;
    private SpotsDialog dialog;

    public ImageDownloader(Context context, SpotsDialog newDialog){
        mContext = context;
        dialog = newDialog;
    }

    protected void onPreExecute() {
        dialog.show();
        super.onPreExecute();
        Log.e(TAG, "Download begin " );
    }

    @Override
    protected Void doInBackground(String... params) {
        try {


            URL url = new URL(Constans.ENDPOINT_ADDR+"/images");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

//            InputStream inputStream;
//            int status = connection.getResponseCode();
//            if (status != HttpURLConnection.HTTP_OK)
//                inputStream = connection.getErrorStream();
//            else
//                inputStream = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();
            System.out.println("output===============" + br);
            ArrayList<HashMap<String, String>> urlList ;
            urlList = null;
            while((line = br.readLine()) != null ) {
                responseOutput.append(line);
            }
            br.close();

            urlList =  JsonHelper.parseResponse(responseOutput.toString());
            Log.e(TAG, "Json parsing completed: " + responseOutput);

            if(urlList.size() == 0){

                DownloadHelper.downloadImage("http://cdn3.nflximg.net/images/3093/2043093.jpg","tes",mContext);

            }else{

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
                            DownloadHelper.downloadImage(imageUrl,id,mContext);
                        }

                    }

                    Log.e(TAG, "Json parsing completed: " + title);

                }


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e(TAG, "Json parsing completed: " );
        dialog.dismiss();
    }



}
