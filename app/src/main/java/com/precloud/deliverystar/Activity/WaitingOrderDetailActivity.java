package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

public class WaitingOrderDetailActivity extends AppCompatActivity {
    TextView txt_name,txt_order_no,txt_payment_mode,txt_hotel_name,txt_hotel_address,txt_dest_address,receipt_name,receipt_number,txt_amt,txt_estimated_time,txt_ref_no;
    Button btn_way_to_hotel,btn_showway,btn_accept,btn_reject;
    OrderResponse orderResponse;
    ImageView img_back;
    ProgressDialog progressDialog;
    TextView txt_labelamount,user_mob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_order_detail);
        initViews();
    }

    private void initViews(){
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("");
        btn_way_to_hotel = findViewById(R.id.btn_way_to_hotel);
        txt_payment_mode = findViewById(R.id.txt_payment_mode);
        txt_order_no = findViewById(R.id.txt_order_no);
        txt_hotel_name = findViewById(R.id.txt_hotel_name);
        txt_hotel_address = findViewById(R.id.txt_hotel_address);
        receipt_name = findViewById(R.id.receipt_name);
        receipt_number = findViewById(R.id.receipt_number);
        txt_dest_address = findViewById(R.id.txt_dest_address);
        btn_showway = findViewById(R.id.btn_showway);
        img_back = findViewById(R.id.img_back);
        txt_amt= findViewById(R.id.txt_amt);
        txt_estimated_time = findViewById(R.id.txt_estimated_time);
        txt_ref_no = findViewById(R.id.txt_ref_no);
        btn_accept = findViewById(R.id.btn_accept);
        btn_reject = findViewById(R.id.btn_reject);
        txt_labelamount = findViewById(R.id.txt_labelamount);
        user_mob = findViewById(R.id.user_mob);
        orderResponse = (OrderResponse) getIntent().getSerializableExtra("order");




        txt_order_no.setText(orderResponse.getOrderId());
        txt_hotel_name.setText(orderResponse.getRestaurantName());
        txt_payment_mode.setText(orderResponse.getPaymentMode());
        txt_ref_no.setText(orderResponse.getReferenceNo());
        if(orderResponse.getPaymentAmount()!=null && !orderResponse.getPaymentAmount().equals("")) {

            txt_amt.setText( orderResponse.getPaymentAmount());

        }else {
            txt_amt.setVisibility(View.GONE);
            txt_labelamount.setVisibility(View.GONE);
        }
        txt_estimated_time.setText(orderResponse.getOrder_estimated_pickup_time());

        txt_hotel_address.setText(orderResponse.getSourceLocation());
        receipt_name.setText(orderResponse.getRecipientName());
        receipt_number.setText(orderResponse.getRecipientNumber());
        txt_dest_address.setText(orderResponse.getDestinationLocation());
        user_mob.setText(orderResponse.getRestaurant_phone());


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order_Aceept_Reject("2",orderResponse.getId());
            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order_Aceept_Reject("5",orderResponse.getId());
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
    private void Order_Aceept_Reject(String status, String orderid){
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().acceptOrderStatus(Storage.getInstance().getString(Constants.Id), orderid,status,"","").enqueue(
                new Callback<UpdateOrderStatusWrapper>() {
                    @Override
                    public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                        if (response.isSuccessful()){
                            hideProgressDialog();
                            if(response.body()!=null){
                                if(response.body().getStatus().equals("1")) {
                                    Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Home_Menu_Activity.class);
                                    startActivity(intent);
                                }

                            }
                        }else {
                            hideProgressDialog();
                            Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    public  void  showProgressDialog(){
        if (progressDialog==null ) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("loading");
            if (progressDialog != null && !progressDialog.isShowing()) {
                progressDialog.show();
            }
        }

    }

    public  void  hideProgressDialog() {

        if (progressDialog != null) {
            if (progressDialog.isShowing() && progressDialog.getWindow() != null) {
                progressDialog.dismiss();
                progressDialog = null;

            }
        }
    }
}
