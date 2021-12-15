package com.precloud.deliverystar.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.work.WorkManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
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
import com.precloud.deliverystar.Service.RiderOnlineStatusService;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;


public class RiderStatus_Fragment extends Fragment {
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
    View view;

    CardView card_onduty,card_off_duty,card_onbreak;
     RadioButton radio_onduty,radio_offduty,radio_breakduty;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_rider_status_,container,false);
        initViews();
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initViews(){

        mWorkManager = WorkManager.getInstance(getActivity());
        card_onduty =  view.findViewById(R.id.card_onduty);
        card_off_duty =  view.findViewById(R.id.card_off_duty);
        card_onbreak =  view.findViewById(R.id.card_onbreak);
        radio_breakduty = view.findViewById(R.id.radio_breakduty);
        radio_offduty = view.findViewById(R.id.radio_offduty);
        radio_onduty = view.findViewById(R.id.radio_onduty);

        LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), "Please enable location services", Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(myIntent);

        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            getRiderStaus();

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }

        getRiderStaus();

       /* switch_checkin = view.findViewById(R.id.switch_checkin);
        img_back = view.findViewById(R.id.img_back);*/
        //   getRiderStaus();
//       Log.e("status",Storage.getInstance().getString(Constants.RiderStatus));



        card_onduty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateOrderStatus("1");
                radio_onduty.setChecked(true);
                radio_breakduty.setChecked(false);
                radio_offduty.setChecked(false);
               // radio_offduty.setChecked(false);

            }
        });


        card_off_duty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateOrderStatus("0");
                radio_offduty.setChecked(true);
                radio_breakduty.setChecked(false);
                radio_onduty.setChecked(false);
                getActivity().getApplicationContext().stopService(new Intent(getActivity(), RiderOnlineStatusService.class));
                //radio_onduty.setChecked(false);
            }
        });

        card_onbreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateOrderStatus("2");
                radio_breakduty.setChecked(true);
                radio_offduty.setChecked(false);
                radio_onduty.setChecked(false);
                getActivity().getApplicationContext().stopService(new Intent(getActivity(), RiderOnlineStatusService.class));
            }
        });

/*
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
*/


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
                               getActivity(). startService(new Intent(getActivity(), RiderOnlineStatusService.class));

                                //  Utils.scheduleJob(getApplicationContext());
                            }else {
                                if (online_status.equals("0")){
                                    Storage.getInstance().setString(Constants.RiderStatus,"0");
                                    getActivity().getApplicationContext().stopService(new Intent(getActivity(), RiderOnlineStatusService.class));

                                }else {
                                    Storage.getInstance().setString(Constants.RiderStatus,"2");
                                    getActivity().getApplicationContext().stopService(new Intent(getActivity(), RiderOnlineStatusService.class));

                                }

                            }


                            //  Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }

                }else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();

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
                                    radio_onduty.setChecked(true);
                                    radio_offduty.setChecked(false);
                                    radio_breakduty.setChecked(false);

                                    Storage.getInstance().setString(Constants.RiderStatus,"1");
                                    getActivity().startService(new Intent(getActivity(), RiderOnlineStatusService.class));
                                }else {
                                    if (getRiderStatusResponseList.get(0).getOnlineStatus().equals("0")) {

                                        Storage.getInstance().setString(Constants.RiderStatus, "0");
                                        radio_offduty.setChecked(true);
                                        radio_breakduty.setChecked(false);
                                        radio_onduty.setChecked(false);


                                        getActivity().getApplicationContext().stopService(new Intent(getActivity(), RiderOnlineStatusService.class));

                                    }else {
                                        Storage.getInstance().setString(Constants.RiderStatus, "2");
                                        radio_breakduty.setChecked(true);
                                        radio_offduty.setChecked(false);
                                        radio_onduty.setChecked(false);


                                        getActivity().getApplicationContext().stopService(new Intent(getActivity(), RiderOnlineStatusService.class));
                                    }


                                }


                            }
                        }
                    }

                }else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetRiderStatusWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getActivity());
        final String path = getString(R.string.firebase_path) + "/" + getString(R.string.transport_id);
        int permission = ContextCompat.checkSelfPermission(getActivity(),
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
