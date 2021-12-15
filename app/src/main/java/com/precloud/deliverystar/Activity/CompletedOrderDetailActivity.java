package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.R;

public class CompletedOrderDetailActivity extends AppCompatActivity {
    TextView txt_name,txt_order_no,txt_payment_mode,txt_hotel_name,txt_hotel_address,txt_dest_address,receipt_name,receipt_number,txt_phn,txt_estimated_time,txt_amt,txt_ref_no;
    Button btn_way_to_hotel,btn_showway;
    OrderResponse orderResponse;
    ImageView img_back;
    TextView txt_labelamount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_order_detail);
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
        txt_phn = findViewById(R.id.txt_phn);
        txt_estimated_time = findViewById(R.id.txt_estimated_time);
        img_back = findViewById(R.id.img_back);
        txt_amt = findViewById(R.id.txt_amt);
        txt_ref_no = findViewById(R.id.txt_ref_no);
        txt_labelamount = findViewById(R.id.txt_labelamount);

        orderResponse = (OrderResponse) getIntent().getSerializableExtra("order");




        txt_order_no.setText(orderResponse.getOrderId());
        txt_hotel_name.setText(orderResponse.getRestaurantName());
        txt_payment_mode.setText(orderResponse.getPaymentMode());
        if(orderResponse.getPaymentAmount()!=null && !orderResponse.getPaymentAmount().equals("")) {

            txt_amt.setText( orderResponse.getPaymentAmount());
        }else {
            txt_amt.setVisibility(View.GONE);
            txt_labelamount.setVisibility(View.GONE);
        }

        txt_hotel_address.setText(orderResponse.getSourceLocation());
        receipt_name.setText(orderResponse.getRecipientName());
        receipt_number.setText(orderResponse.getRecipientNumber());
        txt_dest_address.setText(orderResponse.getDestinationLocation());
        txt_estimated_time.setText(orderResponse.getOrder_estimated_pickup_time());
        txt_phn.setText(orderResponse.getRestaurant_phone());
        txt_ref_no.setText(orderResponse.getReferenceNo());

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

}
