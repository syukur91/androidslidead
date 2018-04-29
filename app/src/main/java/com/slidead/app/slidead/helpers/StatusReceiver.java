package com.slidead.app.slidead.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by syukur on 22/04/18.
 */

public class StatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
//

        try{

            context.startService(new Intent(context.getApplicationContext(), ImageListDownloaderService.class));

        }catch(Exception e){


        }

    }





}
