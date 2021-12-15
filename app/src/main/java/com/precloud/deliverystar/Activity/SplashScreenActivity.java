package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.precloud.deliverystar.Adapter.OrderAdapter;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.Storage;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseApp.initializeApp(this);

        splashTime();
    }
    private void splashTime() {

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                   // Log.e("Firebase", "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();
                Storage.getInstance().setString(Constants.TOKEN,token);

                // Log and toast
                String msg = token;
                Log.e("token", msg);
                //Toast.makeText(SplashScreenActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

        new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Storage.getInstance().getString(Constants.Id)!=null && !Storage.getInstance().getString(Constants.Id).equals("")){

                        Intent intent = new Intent(SplashScreenActivity.this, Home_Menu_Activity.class);
                        SplashScreenActivity.this.startActivity(intent);
                        SplashScreenActivity.this.finish();


                    }else {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        SplashScreenActivity.this.startActivity(intent);
                        SplashScreenActivity.this.finish();

                    }



                }
            }, 1000);
        }
    }
