package com.slidead.app.slidead;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.slidead.app.slidead.helpers.LocationHelper;
import com.slidead.app.slidead.helpers.PostClass;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends Activity {

    Button button;
    private FirebaseAuth auth;
    private SpotsDialog dialog;

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


        AsyncTask<String,Void, Void> task =new PostClass(LoginActivity.this);

        task.execute(latitu,longitu);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        addListenerOnButton();

    }


    public void addListenerOnButton() {

        button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String email = ((EditText)findViewById(R.id.email)).getText().toString().trim();
                final String password = ((EditText)findViewById(R.id.password)).getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog = new SpotsDialog(LoginActivity.this,"Authenticating..");

                dialog.show();


                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                dialog.dismiss();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        ((EditText)findViewById(R.id.password)).setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });



            }

        });

    }
}
