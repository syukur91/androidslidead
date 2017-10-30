package com.slidead.app.slidead;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.Toast;

import com.slidead.app.slidead.helpers.LocationHelper;
import com.slidead.app.slidead.helpers.PostClass;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        File dir = new File(Environment.getExternalStorageDirectory().toString()+"/loocads");
        if (dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        LocationHelper loc = new LocationHelper();
        HashMap<String,String> alt = loc.getLocation(getApplicationContext());
        String latitu = "";
        String longitu = "";

        for (Map.Entry<String, String> entrySet : alt.entrySet()) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
            if(key == "latitude") {
                latitu = value;
            }
            if(key == "longitude") {
                longitu = value;
            }
        }

        Toast.makeText(this,"Send current position latitude:" + latitu + " longitude: "+ longitu, Toast.LENGTH_SHORT).show();

        new PostClass(getApplicationContext()).execute(latitu,longitu);

        addListenerOnButton();
    }


    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }

        });

    }
}
