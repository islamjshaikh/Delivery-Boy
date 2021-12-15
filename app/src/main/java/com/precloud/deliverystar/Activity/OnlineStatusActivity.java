package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.precloud.deliverystar.Model.GetRiderStatusResponse;
import com.precloud.deliverystar.Model.GetRiderStatusWrapper;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Service.RiderLocationUpdateService;
import com.precloud.deliverystar.Service.UploadWorker;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;
import com.precloud.deliverystar.Utility.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OnlineStatusActivity extends AppCompatActivity {
               TextView txt_name;
    Switch switch_checkin;
    String lat,lng;
    List<GetRiderStatusResponse> getRiderStatusResponseList = new ArrayList<>();
    ImageView img_back;
    ProgressDialog progressDialog;
    private static final int PERMISSIONS_REQUEST = 1;

    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;
    private AlertDialog alertLocationDialog = null;
    private static final int JOBID = 110;
    private JobScheduler myScheduler;
    private JobInfo myjobInfo;
    WorkManager mWorkManager;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_status);
        initViews();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(){

        mWorkManager = WorkManager.getInstance(getApplicationContext());


        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);

        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            getRiderStaus();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

            txt_name =  findViewById(R.id.txt_name);
        txt_name.setText("Online Status");
        switch_checkin = findViewById(R.id.switch_checkin);
        img_back = findViewById(R.id.img_back);
    //   getRiderStaus();
//       Log.e("status",Storage.getInstance().getString(Constants.RiderStatus));


       img_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });



        switch_checkin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked){
                    UpdateOrderStatus("1");

                }else {
                    UpdateOrderStatus("0");

                }
            }
        });


    }




    private void UpdateOrderStatus(final String online_status){
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().updateRiderStatus(Storage.getInstance().getString(Constants.Id),online_status,lat,lng).enqueue(new Callback<UpdateOrderStatusWrapper>() {
            @Override
            public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                if (response.isSuccessful()){
                    hideProgressDialog();
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("1")){
                            if (online_status.equals("1")){
                                Storage.getInstance().setString(Constants.RiderStatus,"1");
                              //  startService(new Intent(getApplicationContext(), RiderOnlineStatusService.class));

                                //  Utils.scheduleJob(getApplicationContext());
                            }else {
                                Storage.getInstance().setString(Constants.RiderStatus,"0");
                             //   stopService(new Intent(getApplicationContext(), RiderOnlineStatusService.class));

                            }


                          //  Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }

                }else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();

            }
        });

    }


    private void getRiderStaus(){
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().getRiderStatus(Storage.getInstance().getString(Constants.Id)).enqueue(new Callback<GetRiderStatusWrapper>() {
            @Override
            public void onResponse(Call<GetRiderStatusWrapper> call, Response<GetRiderStatusWrapper> response) {
                if (response.isSuccessful()){
                    hideProgressDialog();
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("1")){
                            if (response.body().getResult()!=null){
                                getRiderStatusResponseList = response.body().getResult();
                                if (getRiderStatusResponseList.get(0).getOnlineStatus().equals("1")){
                                    switch_checkin.setChecked(true);
                                    Storage.getInstance().setString(Constants.RiderStatus,"1");
                                  //  startService(new Intent(getApplicationContext(), RiderOnlineStatusService.class));
                                }else {
                                    Storage.getInstance().setString(Constants.RiderStatus,"0");
                                    switch_checkin.setChecked(false);
                                  //  stopService(new Intent(getApplicationContext(), RiderOnlineStatusService.class));


                                }


                            }
                        }
                    }

                }else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetRiderStatusWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading");
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }
    }

    public void hideProgressDialog() {

        if (progressDialog != null) {
            if (progressDialog.isShowing() && progressDialog.getWindow() != null) {
                progressDialog.dismiss();
                progressDialog = null;

            }
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Start the service when the permission is granted
            getRiderStaus();
        } else {

        }
    }




    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lat = String.valueOf(location.getLatitude());
                    lng  = String.valueOf(location.getLongitude());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fail", "unable to connect");
                }
            });
        }
    }





}
