package com.precloud.deliverystar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.precloud.deliverystar.R;

public class OrderDetails extends AppCompatActivity {
 TextView txt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        initViews();
    }
    private void initViews(){
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Order Details");
    }
}
