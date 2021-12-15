package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager.widget.ViewPager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.precloud.deliverystar.Adapter.TabAdapter;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.Model.OrderWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Service.TrackerService;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {
    TextView txt_name;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        initViews();
    }

    private void initViews() {

        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Orders");
       img_back = findViewById(R.id.img_back);

       img_back.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(),WaitngActivity.class);
               startActivity(intent);
               finish();
           }
       });



        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Pending Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Completed Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Cancelled Orders"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final TabAdapter adapter = new TabAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        final BottomNavigationView navView = findViewById(R.id.nav_view);

        // navView.getMenu().getItem(0).setChecked(false);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);



        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              //  removeColor(navView);


                if(item.getItemId()==R.id.navigation_order){
                    Intent orderintent = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(orderintent);
                    return true;
                }

                if(item.getItemId()==R.id.navigation_waiting){
                    item.setIcon(R.drawable.waiting_color);
                    Intent waiting = new Intent(getApplicationContext(), WaitngActivity.class);
                    startActivity(waiting);
                    return true;
                }

                if(item.getItemId()==R.id.navigation_earning){
                    Intent earningintent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(earningintent);
                    return true;
                }
               /* switch (item.getItemId()) {

                    case R.id.navigation_order:

                        Intent orderintent = new Intent(getApplicationContext(), OrderActivity.class);
                        startActivity(orderintent);
                        return true;
                    case R.id.navigation_waiting:
                        Intent waiting = new Intent(getApplicationContext(), WaitngActivity.class);
                        startActivity(waiting);
                        return true;
                        case R.id.navigation_earning:
                        Intent earningintent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(earningintent);
                    return true;




                }*/
                return true;
            }

        });
    }
    private void removeColor(BottomNavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }
}