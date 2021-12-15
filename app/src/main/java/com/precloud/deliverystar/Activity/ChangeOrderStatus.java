package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.ui.AppBarConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.precloud.deliverystar.Fragment.Waiting_Order_Fragment;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Service.TrackerMapActivity;
import com.precloud.deliverystar.Service.TrackerService;
import com.precloud.deliverystar.Service.TrackingMainActivity;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

 public class ChangeOrderStatus extends AppCompatActivity {
 TextView txt_remark, txt_name,txt_order_no,txt_payment_mode,txt_hotel_name,txt_hotel_address,txt_dest_address,receipt_name,receipt_number,txt_hotel_phn,txt_estimated_time,txt_amt,txt_ref_no;
 Button btn_way_to_hotel,btn_showway,btn_pick,btn_deliver,btn_arriveathotel;
 Button btn_source_loc,btn_dest_loc;
 OrderResponse orderResponse;
 String lat,lng;
 String flag="";
 Double src_lat,src_lng,dest_lat,dest_lng;
 String order_status;
 ProgressDialog progressDialog;
 ImageView img_back;
 int backButtonCount = 0;
 TextView txt_labelamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_status);
        initViews();

    }

    private void initViews(){
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Order Detail");
        btn_way_to_hotel = findViewById(R.id.btn_way_to_hotel);
        txt_payment_mode = findViewById(R.id.txt_payment_mode);
        txt_order_no = findViewById(R.id.txt_order_no);
        txt_hotel_name = findViewById(R.id.txt_hotel_name);
        txt_hotel_address = findViewById(R.id.txt_hotel_address);
        receipt_name = findViewById(R.id.receipt_name);
        receipt_number = findViewById(R.id.receipt_number);
        txt_dest_address = findViewById(R.id.txt_dest_address);
        btn_showway = findViewById(R.id.btn_showway);
        btn_pick = findViewById(R.id.btn_pick);
        btn_deliver = findViewById(R.id.btn_deliver);
        txt_estimated_time  = findViewById(R.id.txt_estimated_time);
        txt_hotel_phn = findViewById(R.id.txt_hotel_phn);
        btn_dest_loc = findViewById(R.id.btn_dest_loc);
        btn_source_loc = findViewById(R.id.btn_source_loc);
        txt_amt = findViewById(R.id.txt_amt);
        txt_ref_no= findViewById(R.id.txt_ref_no);
        img_back = findViewById(R.id.img_back);
        btn_arriveathotel = findViewById(R.id.btn_arriveathotel);
        txt_labelamount = findViewById(R.id.txt_labelamount);
        txt_remark = findViewById(R.id.txt_remark);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Home_Menu_Activity.class);
                startActivity(intent);
                finish();
            }
        });


        orderResponse = (OrderResponse) getIntent().getSerializableExtra("order");

        try {
            if (orderResponse != null && orderResponse.getSourceLatLong() != null && orderResponse.getDestinationLatLong() != null) {
                JSONObject jsonObject_source = new JSONObject(orderResponse.getSourceLatLong());
                JSONObject jsonObject_dest = new JSONObject(orderResponse.getDestinationLatLong());
                src_lat = Double.parseDouble(jsonObject_source.getString("latitude"));
                src_lng = Double.parseDouble(jsonObject_source.getString("longitude"));
                dest_lat = Double.parseDouble(jsonObject_dest.getString("latitude"));
                dest_lng = Double.parseDouble(jsonObject_dest.getString("longitude"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(orderResponse.getOrderStatusId().equals("2")){
            btn_pick.setVisibility(View.GONE);
            btn_deliver.setVisibility(View.GONE);
            btn_way_to_hotel.setVisibility(View.VISIBLE);
            btn_arriveathotel.setVisibility(View.GONE);
        }else {
            if (orderResponse.getOrderStatusId().equals("3")){
                btn_deliver.setVisibility(View.VISIBLE);
                btn_pick.setVisibility(View.GONE);
                btn_way_to_hotel.setVisibility(View.GONE);
                btn_arriveathotel.setVisibility(View.GONE);
            }else {
                if (orderResponse.getOrderStatusId().equals("7")){
                    btn_way_to_hotel.setVisibility(View.GONE);
                    btn_arriveathotel.setVisibility(View.VISIBLE);
                    btn_deliver.setVisibility(View.GONE);
                    btn_pick.setVisibility(View.GONE);
                }else {
                    if(orderResponse.getOrderStatusId().equals("8")){
                        btn_pick.setVisibility(View.VISIBLE);
                        btn_deliver.setVisibility(View.GONE);
                        btn_way_to_hotel.setVisibility(View.GONE);
                        btn_arriveathotel.setVisibility(View.GONE);

                    }
                }
            }
        }




        btn_way_to_hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag  ="waytohotel";
                changeOrderStatus("7",orderResponse.getId());
                /*Intent intent = new Intent(getApplicationContext(), TrackerActivity.class);

                startActivity(intent);*/
            }
        });

        btn_arriveathotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag  ="arriveathotel";
                changeOrderStatus("8",orderResponse.getId());
            }
        });


        btn_source_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", src_lat, src_lng, "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        btn_dest_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", dest_lat, dest_lng, "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                try
                {
                    startActivity(intent);
                }
                catch(ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    }
                    catch(ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });




        btn_showway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                intent.putExtra("sourcelatlng",orderResponse.getSourceLatLong());
                intent.putExtra("destlatlng",orderResponse.getDestinationLatLong());
                startActivity(intent);
            }
        });

        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag ="pick";
                changeOrderStatus("3",orderResponse.getId());
            }
        });

        btn_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag ="deliver";
                if(orderResponse.getPaymentMode()!=null && orderResponse.getPaymentMode().equalsIgnoreCase("cod")){
                    showconfirmationdialog();
                }
                if(orderResponse.getPaymentMode().equalsIgnoreCase("pos")){
                    showconfirmationdialog();
                }
                else {
                    changeOrderStatus("4",orderResponse.getId());

                }


                stopService(new Intent(getApplicationContext(), TrackerService.class));
            }
        });




        txt_order_no.setText(orderResponse.getOrderId());
        txt_hotel_name.setText(orderResponse.getRestaurantName());
        txt_payment_mode.setText(orderResponse.getPaymentMode());
        if(orderResponse.getPaymentAmount()!=null && !orderResponse.getPaymentAmount().equals("")) {

            txt_amt.setText(orderResponse.getPaymentAmount());
        }else {
            txt_amt.setVisibility(View.GONE);
            txt_labelamount.setVisibility(View.GONE);
        }

        txt_hotel_address.setText(orderResponse.getSourceLocation());
        receipt_name.setText(orderResponse.getRecipientName());
        receipt_number.setText(orderResponse.getRecipientNumber());
        txt_dest_address.setText(orderResponse.getDestinationLocation());
        txt_hotel_phn.setText(orderResponse.getRestaurant_phone());
        txt_estimated_time.setText(orderResponse.getOrder_estimated_pickup_time());
       txt_ref_no.setText(orderResponse.getReferenceNo());
        txt_remark.setText(orderResponse.getRemark());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_order:
                        Intent orderintent = new Intent(getApplicationContext(), OrderActivity.class);
                        startActivity(orderintent);
                        break;
                    case R.id.navigation_waiting:

                        break;
                    case R.id.navigation_earning:
                        Intent earningintent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(earningintent);
                        break;




                }
                return false;
            }

        });
    }

    private void changeOrderStatus(String status,String OrderId){
        showProgressDialog();
        requestLocationUpdates();

        DeliveryStarApplication.getmAPIService().updateOrderStatus(Storage.getInstance().getString(Constants.Id),OrderId,status,lat,lng).enqueue(new Callback<UpdateOrderStatusWrapper>() {
            @Override
            public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                if (response.isSuccessful()){
                    hideProgressDialog();
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("1")){

                            Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            if (flag.equals("waytohotel")) {
                                order_status ="7";
                                btn_pick.setVisibility(View.GONE);
                                btn_way_to_hotel.setVisibility(View.GONE);
                                btn_arriveathotel.setVisibility(View.VISIBLE);
                                Storage.getInstance().setString(Constants.OrderId,orderResponse.getId());
                               // Log.e("OrderId",Storage.getInstance().getString(Constants.OrderId));
                               // Intent intent = new Intent(getApplicationContext(), TrackerActivity.class);
                                //startActivity(intent);
                            }else {
                                if (flag.equals("pick")){
                                    order_status="2";
                                    btn_way_to_hotel.setVisibility(View.GONE);
                                    btn_pick.setVisibility(View.GONE);
                                    btn_arriveathotel.setVisibility(View.GONE);
                                    btn_deliver.setVisibility(View.VISIBLE);
                                }else {
                                    if (flag.equals("deliver")){
                                        order_status="4";
                                        showAlert(response.body().getMessage());
                                        btn_way_to_hotel.setVisibility(View.GONE);
                                        btn_pick.setVisibility(View.GONE);
                                        btn_deliver.setVisibility(View.VISIBLE);
                                        btn_arriveathotel.setVisibility(View.GONE);
                                        btn_deliver.setText("Order Completed");
                                        btn_deliver.setEnabled(false);
                                        /*
                                        Intent intent = new Intent(ChangeOrderStatus.this, Home_Menu_Activity.class);
                                        ChangeOrderStatus.this.startActivity(intent);
                                        ChangeOrderStatus.this.finish();
                                        */

                                       // Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
                                        //startActivity(intent);
                                    }else {
                                      if(flag.equals("arriveathotel")){
                                          btn_way_to_hotel.setVisibility(View.GONE);
                                          btn_pick.setVisibility(View.VISIBLE);
                                          btn_deliver.setVisibility(View.GONE);
                                          btn_arriveathotel.setVisibility(View.GONE);

                                        }
                                    }
                                }
                            }
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
     @Override
     public void onBackPressed() {

         if (backButtonCount >= 1){
             finishAffinity();
             finish();
         }else{
             Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_LONG).show();
             backButtonCount++;
         }
     }


     @Override
     protected void onPause() {
         super.onPause();
         // Toast.makeText(getApplicationContext(), "onPause", Toast.LENGTH_LONG).show();
         try {
             finishAffinity();
             finish();
         }
         catch (Exception e){}
     }







     public void showAlert(String message) {
         // 1. Instantiate fan <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
         AlertDialog.Builder builder = new AlertDialog.Builder(ChangeOrderStatus.this);
         builder.setPositiveButton(
                 "Yes",
                 new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                         dialog.cancel();
                         Intent intent = new Intent(ChangeOrderStatus.this, Home_Menu_Activity.class);
                         ChangeOrderStatus.this.startActivity(intent);
                         ChangeOrderStatus.this.finish();
                     }
                 });

// 2. Chain together various setter methods to set the dialog characteristics
         builder.setMessage(message)
                 .setTitle("Order Staus");

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
         AlertDialog dialog = builder.create();
         dialog.show();
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

     private void showconfirmationdialog(){

        View dialogView=View.inflate(this,R.layout.confirmation_doalog,null);
         final Dialog dialog=new Dialog(this);
         dialog.setContentView(dialogView);
         dialog.show();
         TextView txt_dialog =dialogView.findViewById(R.id.txt_dialog);
         Button btn_yes = dialogView.findViewById(R.id.btn_yes);
         Button btn_no = dialogView.findViewById(R.id.btn_no);



         txt_dialog.setText("Have you collected the amount"+" " + orderResponse.getPaymentAmount()+"?");

         btn_yes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 dialog.dismiss();
                 changeOrderStatus("4",orderResponse.getId());

             }
         });

         btn_no.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
               //  btn_deliver.setClickable(false);
                 dialog.dismiss();
             }
         });


     }
}
