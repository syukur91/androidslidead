package com.slidead.app.slidead.helpers;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Syukur on 7/22/2017.
 */

public class DownloadHelper extends Activity {

    public static void  downloadImage(String url, String id, Context c) {

        DownloadManager.Request request = null;
        File direct = new File(Environment.getExternalStorageDirectory() + "/loocads");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        try {
            File file = new File(Environment.getExternalStorageDirectory() +"/loocads", id+".jpg");
            if(file.exists() == false){

                request = new DownloadManager.Request(Uri.parse(url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.setVisibleInDownloadsUi(false);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                request.setDestinationInExternalFilesDir(c,Environment.getDataDirectory() +"/loocads", id+".jpg");

                if(file.exists() == false) {
                    final DownloadManager dm = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
                    dm.enqueue(request);
                }

            }

        } catch (IllegalArgumentException e) {
            Log.d("Slider Demo","Error" + e.getMessage());
        }

        /* allow mobile and WiFi downloads */

//        request.setTitle("DM Example");
//        request.setDescription("Downloading file");

         /* we let the user see the download in a notification */
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        /* Try to determine the file extension from the url. Only allow image types. You
         * can skip this check if you only plan to handle the downloaded file manually and
         * don't care about file managers not recognizing the file as a known type */
//        String[] allowedTypes = {"png", "jpg", "jpeg", "gif", "webp"};
//        String suffix = url.substring(url.lastIndexOf(".") + 1).toLowerCase();

//        if (!Arrays.asList(allowedTypes).contains(suffix)) {
//            Log.d("Invalid file extension"," Allowed types: \\n");
//
//            for (String s : allowedTypes) {
//                Log.d("\n","."+s);
//            }
//        }




          /* set the destination path for this download */
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS +File.separator + "2043093", name + "." + suffix);



    }

    public static void saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String fname = "Image-New.jpg";
        File file = new File (myDir, fname);
        if (file.exists ()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean verifyLatestDownload(Context context){
        File directory = new File(Environment.getExternalStorageDirectory() + "/loocads");

        if (!directory.exists()) {
            return false;
        }


        File[] files = directory.listFiles();
        if (files.length == 0){
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Log.d("Files", "Size: "+ files.length);

        File latestFile = lastFileModified(Environment.getExternalStorageDirectory() + "/loocads");

        String latestDownloadedDate = sdf.format(latestFile.lastModified());


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String currentDate = df.format(c);


        if (latestDownloadedDate.equals(currentDate) ){

            return true;
        }
        else {
            return false;
        }

    }

    public static File lastFileModified(String dir) {
        File fl = new File(dir);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if (file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice;
    }

    public static void cleanDuplicateImage(){

        String path = Environment.getExternalStorageDirectory().toString()+"/loocads";
        File f = new File(path);
        File files[] = f.listFiles();

        for (File item : files){
            if(item.getName().contains("-1.jpg")){
                item.delete();
            }
        }

    }

}
