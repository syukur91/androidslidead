package com.slidead.app.slidead.helpers.playlist;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import com.slidead.app.slidead.R;
import com.slidead.app.slidead.helpers.JsonHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import dmax.dialog.SpotsDialog;

import static android.content.ContentValues.TAG;

public class PlaylistDownloader extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private SpotsDialog dialog;

    public PlaylistDownloader(Context context){
        mContext = context;
        dialog = new SpotsDialog(mContext, R.style.API);
    }

    protected void onPreExecute() {
//        dialog.show();
        super.onPreExecute();
        Log.e(TAG, "Post begin " );
    }

//    private final Context context;
//
//    public PlaylistDownloader(Context c){
//
//        this.context = c;
////            this.error = status;
////            this.type = t;
//    }

    @Override
    protected Void doInBackground(String... params) {
        try {

            String latitude = params[0];
            String longitude = params[1];

            URL url = new URL("http://45.76.178.16:4443/playlist"); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
            httpURLConnection.connect();


            JSONObject jsonObject = new JSONObject();
            String deviceId = android.provider.Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            jsonObject.put("latitude",Double.parseDouble("-6.87634032307858"));
            jsonObject.put("longitude",Double.parseDouble("107.60182648419288"));
            jsonObject.put("deviceId",deviceId);


            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(jsonObject.toString());
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
            urlList =  JsonHelper.parseResponse(responseOutput.toString());
            JsonHelper.saveJson(mContext, responseOutput.toString());
            Log.e(TAG, "Json parsing completed: " + responseOutput);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e(TAG, "Json parsing completed: " );
//        dialog.dismiss();
    }

}
