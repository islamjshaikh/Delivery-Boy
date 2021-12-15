package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.precloud.deliverystar.Adapter.OrderAdapter;
import com.precloud.deliverystar.Adapter.Waiting_adapter;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.Model.OrderWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WaitngActivity extends AppCompatActivity {
    TextView txt_name;
    public static Date CurrentDate, SelectedDate;
    public static String DOB;
    public static EditText edit_date;
    List<OrderResponse> orderResponseList = new ArrayList<>();
    RecyclerView recy_completed_order;
    Waiting_adapter orderAdapter;
    Waiting_adapter waiting_adapter;
    ImageView img_back;
    ProgressDialog progressDialog;
    Handler handler = new Handler();
    Runnable runnable;
    int delay = 15*1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitng);
        iniitViews();
    }

    private void iniitViews() {
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Waiting Orders");
        edit_date = findViewById(R.id.edit_date);
        recy_completed_order = findViewById(R.id.recy_completed_order);
        img_back = findViewById(R.id.img_back);

        GetOrderByRiderId("1");
        //OrderHandler();

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });


        final BottomNavigationView navView = findViewById(R.id.nav_view);
      //  navView.getMenu().getItem(0).setChecked(false);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             //   removeColor(navView);

                if(item.getItemId()==R.id.navigation_order){
                    Intent orderintent = new Intent(getApplicationContext(), OrderActivity.class);
                    startActivity(orderintent);
                    finish();
                    return true;
                }

                if(item.getItemId()==R.id.navigation_waiting){

                    Intent waiting = new Intent(getApplicationContext(), WaitngActivity.class);
                    startActivity(waiting);
                    finish();
                    return true;
                }

                if(item.getItemId()==R.id.navigation_earning){
                    Intent earningintent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(earningintent);
                    finish();
                    return true;
                }

/*
                switch (item.getItemId()) {

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


                }
*/
                return true;
            }

        });
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //Toast.makeText(this.getContext(), ""+day+"/"+month+"/"+year+"" , Toast.LENGTH_SHORT ).show();

            String date = day + "/" + (month + 1) + "/" + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");

            Date myDate = null;
            try {
                myDate = dateFormat.parse(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");


            DOB = df.format(myDate);
            Date c = Calendar.getInstance().getTime();
            Log.e("date", dateFormat.format(c));
            try {
                CurrentDate = dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
                // Log.e("new Date", String.valueOf(CurrentDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SelectedDate = myDate;
            //  Log.e("Selected Date", String.valueOf(SelectedDate));


            edit_date.setText(DOB);
        }
    }


    private void GetOrderByRiderId(String order_status) {
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().getUnassinedOrderByRiderId(Storage.getInstance().getString(Constants.Id), order_status).enqueue(new Callback<OrderWrapper>() {
            @Override
            public void onResponse(Call<OrderWrapper> call, Response<OrderWrapper> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            if (response.body().getResult() != null) {
                                orderResponseList = response.body().getResult();
                                if (orderResponseList.size() > 0 && orderResponseList != null) {


                                    orderAdapter = new Waiting_adapter(getApplicationContext(), orderResponseList);
                                    recy_completed_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    recy_completed_order.setAdapter(orderAdapter);

                                }
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void HandlerGetOrderByRiderId(String order_status) {
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().getUnassinedOrderByRiderId(Storage.getInstance().getString(Constants.Id), order_status).enqueue(new Callback<OrderWrapper>() {
            @Override
            public void onResponse(Call<OrderWrapper> call, Response<OrderWrapper> response) {
                if (response.isSuccessful()) {
                    hideProgressDialog();
                    if (response.body() != null) {
                        if (response.body().getStatus().equals("1")) {
                            if (response.body().getResult() != null) {
                                orderResponseList = response.body().getResult();
                                if (orderResponseList.size() > 0 && orderResponseList != null) {


                                    waiting_adapter = new Waiting_adapter(getApplicationContext(), orderResponseList);
                                    recy_completed_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    recy_completed_order.setAdapter(waiting_adapter);

                                }
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

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

    private void removeColor(BottomNavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);

        }
    }

    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                HandlerGetOrderByRiderId("1");
                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }
}
