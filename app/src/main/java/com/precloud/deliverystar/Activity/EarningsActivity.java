package com.precloud.deliverystar.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.precloud.deliverystar.R;

public class EarningsActivity extends AppCompatActivity {
   TextView txt_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings);
        initViews();
    }
    private void initViews(){
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Earnings");
    }
}
