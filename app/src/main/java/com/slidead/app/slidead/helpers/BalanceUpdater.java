package com.slidead.app.slidead.helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by syukur on 26/07/18.
 */

public class BalanceUpdater extends AsyncTask<String, Void, Void> {

    private Context mContext;
    private SpotsDialog dialog;


    public BalanceUpdater(Context context){
        mContext = context;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        Log.e(TAG, "Download begin " );
    }

    @Override
    protected Void doInBackground(String... strings) {

    try{

        String id = strings[0];

        URL url = new URL(Constans.ENDPOINT_ADDR+"/deduction");
        HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
        httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
        httpURLConnection.connect();

        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("quantity", 1);

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
        while((line = br.readLine()) != null ) {
            responseOutput.append(line);
        }
        br.close();

        output.append(System.getProperty("line.separator") + "Response " + System.getProperty("line.separator") + System.getProperty("line.separator") + responseOutput.toString());
        Log.e(TAG, "Json parsing completed: " + responseOutput);


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
        Log.e(TAG, "Json parsing completed: " );
    }
}
