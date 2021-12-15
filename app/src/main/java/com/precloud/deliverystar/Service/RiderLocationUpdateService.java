package com.precloud.deliverystar.Service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.lang.ref.WeakReference;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RiderLocationUpdateService  extends JobService {

    private MJobExecutor executor;


    @Override
    public boolean onStartJob(final JobParameters params) {

        executor = new MJobExecutor(this, params);
        executor.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        executor.cancel(true);
        return false;
    }

    private static class MJobExecutor extends AsyncTask<Void, Void, String> {
        private WeakReference<JobService> jobServiceReference;
        private JobParameters params;

        MJobExecutor(JobService jobService, JobParameters params) {
            jobServiceReference = new WeakReference<>(jobService);
            this.params = params;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return "Background task running";
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JobService jobService = jobServiceReference.get();
            if (jobService != null) {
                Intent intent = new Intent("JOB_FINISHED");
                intent.putExtra("result", s);
                LocalBroadcastManager.getInstance(jobService).sendBroadcast(intent);
                jobService.jobFinished(params, true);
            }
        }
    }
}