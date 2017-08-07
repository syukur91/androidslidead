package com.slidead.app.slidead.helpers;

/**
 * Created by Syukur on 7/22/2017.
 * Used for getting url image from API
 * Executed via AsyncTask
 */
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.slidead.app.slidead.helpers.HttpHandler;
import com.slidead.app.slidead.helpers.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class UrlGetterHelper extends AsyncTask<Void, Void, Void>   {

    private Context mContext;

    public UrlGetterHelper(Context context){
        mContext = context;
    }

    ArrayList<HashMap<String, String>> contactList;

    String jsonStr = "";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0){
    contactList = new ArrayList<>();

    HttpHandler sh = new HttpHandler();
    // Making a request to url and getting response
//    String url = "https://api.androidhive.info/contacts/";
      String url = "http://10.0.2.2:3000/posts";

        jsonStr = sh.makeServiceCall(url);

    if (jsonStr != null) {
        try {

            JsonHelper.saveJson(mContext,jsonStr);

        } catch (Exception e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
        }

    } else {
        Log.e(TAG, "Couldn't get json from server.");

    }

        return null;
}

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        Log.e(TAG, "Json parsing completed: " + jsonStr);

    }

}
