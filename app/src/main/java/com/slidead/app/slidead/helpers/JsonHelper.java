package com.slidead.app.slidead.helpers;

/**
 * Created by Syukur on 7/23/2017.
 */

import android.content.Context;
import android.media.MediaScannerConnection;
import android.util.Log;

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

            JsonHelper.saveJson(ctx,str);
            JSONObject jsonObj = new JSONObject(str);

            // Getting JSON Array node
            JSONArray contacts = jsonObj.getJSONArray("images");

            // looping through All Contacts
            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);

                String id = c.getString("id");
                String title = c.getString("title");
                String author = c.getString("author");
                String imageurl = c.getString("imageurl");

                HashMap<String, String> list = new HashMap<>();

                list.put("id", id);
                list.put("title", title);
                list.put("author", author);
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

}
