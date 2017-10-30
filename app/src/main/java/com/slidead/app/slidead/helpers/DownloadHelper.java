package com.slidead.app.slidead.helpers;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

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
                request.setDestinationInExternalPublicDir("/loocads", id+".jpg");
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



}
