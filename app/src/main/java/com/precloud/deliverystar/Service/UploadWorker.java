package com.precloud.deliverystar.Service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadWorker extends Worker {
    Context context;
    String lat,lng;
    public UploadWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
         context = getApplicationContext();
        // Do the work here--in this case, upload the images.

  requestLocationUpdates();
        // Indicate whether the work finished successfully with the Result
        return Result.success();
    }





    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        int permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lat = String.valueOf(location.getLatitude());
                    lng  = String.valueOf(location.getLongitude());
                    updateRiderLocation(lat,lng);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("fail", "unable to connect");
                }
            });
        }
    }
    private void updateRiderLocation(String lat,String lng){

        DeliveryStarApplication.getmAPIService().updateRiderLocation(Storage.getInstance().getString(Constants.Id),lat,lng).enqueue(new Callback<UpdateOrderStatusWrapper>() {
            @Override
            public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                if (response.isSuccessful()){
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("1")){
                            Toast.makeText(getApplicationContext(),"loc update"+response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),"\"something went wrong\"",Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
