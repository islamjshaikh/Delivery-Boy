package com.precloud.deliverystar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.precloud.deliverystar.Adapter.Home_Adapter;
import com.precloud.deliverystar.R;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
ListView listView;
List<String> menuList = new ArrayList<>();
Home_Adapter adapter ;
ImageView img_back;
LinearLayout lnr_order,lnr_earning,lnr_settings,lnr_waiting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        initView();
    }

    private void initView(){
        lnr_waiting = findViewById(R.id.lnr_waiting);
        lnr_order = findViewById(R.id.lnr_order);
        lnr_earning = findViewById(R.id.lnr_earning);
        lnr_settings = findViewById(R.id.lnr_settings);


        lnr_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
            }
        });


        lnr_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
                startActivity(intent);
            }
        });
        lnr_earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EarningsActivity.class);
                startActivity(intent);
            }
        });

    }
}
