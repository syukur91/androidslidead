package com.slidead.app.slidead.helpers;

/**
 * Created by Syukur on 7/23/2017.
 */

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.slidead.app.slidead.SplashActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import 	java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class JsonHelper {


    public static void saveJson(Context ctx, String jsonStr){

        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
//            File testFile = new File(ctx.getExternalFilesDir(null), "TestFile.txt");

            File testFile = new File(ctx.getExternalFilesDir(null), "TestFile.txt");

            if(testFile.exists())
                testFile.delete();

            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(jsonStr);
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(ctx,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
        }

    }


    public static String readJson(Context ctx){
        String textFromFile = "";
        // Gets the file from the primary external storage space of the
        // current application.
        File testFile = new File(ctx.getExternalFilesDir(null), "TestFile.txt");
        if (testFile != null) {
            StringBuilder stringBuilder = new StringBuilder();
            // Reads the data from the file
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(testFile));
                String line;

                while ((line = reader.readLine()) != null) {
                    textFromFile += line.toString();
                    textFromFile += "\n";
                }
                reader.close();
            } catch (Exception e) {
                Log.e("ReadWriteFile", "Unable to read the TestFile.txt file.");
            }
        }

        return textFromFile;
    }


    public static ArrayList parseJson(Context ctx, String str){

        ArrayList<HashMap<String, String>> urlList;

        urlList = new ArrayList<>();

        try {

//            JsonHelper.saveJson(ctx,str);
//            JSONObject jsonObj = new JSONObject(str);

            // Getting JSON Array node
//            JSONArray contacts = jsonObj.getJSONArray("images");

            JSONArray contacts = new JSONArray(str);


            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                String id = c.getString("id");
                String title = c.getString("campaignName");
                String imageurl = c.getString("imageUrl");

                HashMap<String, String> list = new HashMap<>();

                list.put("id", id);
                list.put("title", title);
                list.put("url", imageurl);

               // adding contact to contact list
                urlList.add(list);
            }
            return  urlList;

        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return null;
        }

    }

    public static void saveJsonLocal(Context ctx){

        try {
            // Creates a file in the primary external storage space of the
            // current application.
            // If the file does not exists, it is created.
            String str = "{\"images\":[{\"id\":1,\"title\":\"House of Cards\",\"author\":\"typicode\",\"imageurl\":\"http://cdn3.nflximg.net/images/3093/2043093.jpg\"},{\"id\":2,\"title\":\"Hannibal\",\"author\":\"typicode\",\"imageurl\":\"http://s-media-cache-ak0.pinimg.com/originals/1a/ae/74/1aae74b2bd9a5eb26df0338582978bf7.jpg\"},{\"id\":3,\"title\":\"Game of Thrones\",\"author\":\"typicode\",\"imageurl\":\"http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg\"},{\"id\":4,\"title\":\"Big Bang Theory\",\"author\":\"typicode\",\"imageurl\":\"http://tvfiles.alphacoders.com/100/hdclearart-10.png\"}]}";
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            File testFile = new File(ctx.getExternalFilesDir(null), "TestFile.txt");


            if(testFile.exists())
                testFile.delete();

            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(str);
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(ctx,
                    new String[]{testFile.toString()},
                    null,
                    null);
        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
        }

    }



    public static ArrayList parseResponse( String str){

        ArrayList<HashMap<String, String>> urlList;

        urlList = new ArrayList<>();

        try {

//            JsonHelper.saveJson(ctx,str);
//            JSONObject jsonObj = new JSONObject(str);

            // Getting JSON Array node
            JSONArray contacts =  new JSONArray(str);

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                String id = c.getString("id");
                String title = c.getString("campaignName");
                String imageUrl = c.getString("imageUrl");
                String filename = c.getString("id")+".jpg";

                HashMap<String, String> list = new HashMap<>();

                list.put("id", id);
                list.put("title", title);
                list.put("url", imageUrl);
                list.put("filename", filename);

                // adding contact to contact list
                urlList.add(list);
            }
            return  urlList;

        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return null;
        }

    }

    public static ArrayList parseImageListResponse( String str){

        ArrayList<HashMap<String, String>> urlList;

        urlList = new ArrayList<>();

        try {

            JSONObject jsonObj = new JSONObject(str);
            JSONArray contacts = jsonObj.getJSONArray("result");

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);
                String id = c.getString("id");
                String title = c.getString("campaignName");
                String imageUrl = c.getString("imageUrl");
                String filename = c.getString("id")+".jpg";
                HashMap<String, String> list = new HashMap<>();
                list.put("id", id);
                list.put("title", title);
                list.put("url", imageUrl);
                list.put("filename", filename);

                // adding contact to contact list
                urlList.add(list);
            }
            return  urlList;

        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return null;
        }

    }

    public static String parseImageListArray(Context ctx, String str){

        try {
            JSONObject jsonObj = new JSONObject(str);
            JSONArray contacts = jsonObj.getJSONArray("list");
            String result = "";

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                String a = contacts.getString(i);
                result += a+",";
            }


            File testFile = new File(ctx.getExternalFilesDir(null), "ListFile.txt");

            if(testFile.exists())
                testFile.delete();

            if (!testFile.exists())
                testFile.createNewFile();

            // Adds a line to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
            writer.write(result);
            writer.close();
            // Refresh the data so it can seen when the device is plugged in a
            // computer. You may have to unplug and replug the device to see the
            // latest changes. This is not necessary if the user should not modify
            // the files.
            MediaScannerConnection.scanFile(ctx, new String[]{testFile.toString()}, null, null);

            return  result;

        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
            return null;
        } catch (JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return null;
        }

    }


    public static String saveImagePlayed(Context ctx, String str){

        try {


            File testFile = new File(ctx.getExternalFilesDir(null), "ImagePlayed.txt");


            if (!testFile.exists())
                testFile.createNewFile();

            AsyncTask<String,Void, Void> balanceUpdaterTask = new BalanceUpdater(ctx);
            String[] id = str.split("\\.");
            Log.e("FileName:", id[0]);
            balanceUpdaterTask.execute(id[0]);


            return  str;

        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
        }

        return null;

    }


    public static String setTripStatus(Context ctx){

        try {


            File testFile = new File(Environment.getExternalStorageDirectory() , "/loocads/tripstatus.txt");

//            if(testFile.exists())
//                testFile.delete();

            String status = "start";

            if (!testFile.exists()){

                testFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(testFile, true /*append*/));
                writer.write(status);
                writer.close();
                MediaScannerConnection.scanFile(ctx, new String[]{testFile.toString()}, null, null);

            }


            return  status;

        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
            return null;
        }

    }

    public static String getTripStatus(Context ctx){

        try {


            File file = new File(Environment.getExternalStorageDirectory() , "/loocads/tripstatus.txt");

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            //Read text from file
            StringBuilder text = new StringBuilder();

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();


            return  text.toString();

        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
            return null;
        }

    }



}
