package com.precloud.deliverystar.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.precloud.deliverystar.Model.LocationTable;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiderOnlineStatusService extends Service {

    private static final String TAG = TrackerService.class.getSimpleName();
    String rider_status;
    FusedLocationProviderClient client;
    private LocationCallback mLocationCallback;

    @Override
    public IBinder onBind(Intent intent) {return null;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        buildNotification();
        requestLocationUpdates();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildNotification() {
        String stop = "stop";
        // registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = getString(R.string.app_name);
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(channelId);
        notificationChannel.setSound(null, null);

        notificationManager.createNotificationChannel(notificationChannel);
        Notification notification = new Notification.Builder(this, channelId)
                .setContentTitle("Location Tracking Enabled")
              //  .setContentText("Location Tracking Enabled")
                .setSmallIcon(R.drawable.notification)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .build();
        startForeground(1, notification);
    }

    /*protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            // Stop the service when the notification is tapped
            unregisterReceiver(stopReceiver);
         //   stopSelf();
        }
    };*/

    /*
        private void loginToFirebase() {
            // Authenticate with Firebase, and request location updates
            String email = getString(R.string.firebase_email);
            String password = getString(R.string.firebase_password);
            FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "firebase auth success");
                        requestLocationUpdates();
                    } else {
                        Log.d(TAG, "firebase auth failed");
                    }
                }
            });
        }
    */
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String keyValue = intent.getStringExtra("key");
        if(keyValue!=null && keyValue.equals("stop")){
            removeLocationUpdates();
            stopSelf();
       }
        return START_STICKY;
    }

    private void requestLocationUpdates() {
        LocationRequest request = new LocationRequest();
        request.setInterval(10000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
         client = LocationServices.getFusedLocationProviderClient(this);
        final String path = getString(R.string.firebase_path2)+"11";
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            // Request location updates and when an update is
            // received, store the location in Firebase


            client = LocationServices.getFusedLocationProviderClient(this);

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    Location location = locationResult.getLastLocation();
                    updateRiderLocation(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));

                }
            };

            client.requestLocationUpdates(request, mLocationCallback, Looper.myLooper());/* {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference(path);

        Location location = locationResult.getLastLocation();

                    LocationTable locationTable = new LocationTable(
                            locationResult.getLastLocation().getLatitude(),
                            locationResult.getLastLocation().getLongitude(),
                            Storage.getInstance().getString(Constants.OrderId),
                            "ON");

                    if (location != null) {
                        Log.d(TAG, "location update1 " + location);
                       updateRiderLocation(String.valueOf(location.getLatitude()),String.valueOf(location.getLongitude()));
                        // ref.setValue(locationTable);
                       // ref.child("Order"+Storage.getInstance().getString(Constants.OrderId)).push().setValue(locationTable);

                        Log.d("datsabasecreated","cc");


                    }
                }
            }, null);*/
        }
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return false;
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
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            client.removeLocationUpdates(mLocationCallback);

            stopSelf();
        } catch (SecurityException unlikely) {


        }
    }
}