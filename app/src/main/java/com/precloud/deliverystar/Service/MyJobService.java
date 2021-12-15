package com.precloud.deliverystar.Service;

import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nanostuffs on 13-12-2017.
 */

public class MyJobService extends JobService {
    private Storage preferenceHelper;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mLastLocation;

    @Override
    public boolean onStartJob(JobParameters job) {
        // Do some work> here


        getAddress();

        return false; // Answers the question: "Is there still work going on?"
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false; // Answers the question: "Should this job be retried?"
    }


    private void getAddress() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        mFusedLocationClient.getLastLocation()

                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            Log.e("location", "null");
                            return;
                        }

                        mLastLocation = location;
                       String latitude = String.valueOf(mLastLocation.getLatitude());
                        String longitude = String.valueOf(mLastLocation.getLongitude());
                       // Log.e("JobServicelat",latitude+" "+longitude);

                        updateRiderLocation(latitude, longitude);
                        if (!Geocoder.isPresent()) {
                            //Toast.makeText(getApplicationContext(),"No geocoder available",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // If the user pressed the fetch address button before we had the location,
                        // this will be set to true indicating that we should kick off the intent
                        // service after fetching the location.
                      /*  if (mAddressRequested) {
                            startIntentService();
                        }*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "getLastLocation:onFailure", e);
                        Log.e("fail", "unable to connect");
                    }
                });


    }
    private void updateRiderLocation(String lat,String lng){

        DeliveryStarApplication.getmAPIService().updateRiderLocation(Storage.getInstance().getString(Constants.Id),lat,lng).enqueue(new Callback<UpdateOrderStatusWrapper>() {
            @Override
            public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("1")){
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
            }
        });
    }


}