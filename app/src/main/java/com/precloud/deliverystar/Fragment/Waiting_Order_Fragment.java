package com.precloud.deliverystar.Fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.precloud.deliverystar.Activity.OrderActivity;
import com.precloud.deliverystar.Activity.SettingsActivity;
import com.precloud.deliverystar.Activity.WaitngActivity;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Waiting_Order_Fragment  extends Fragment {
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
    View view;
    TextView txt_no_orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_waitng,container,false);
        iniitViews();

        return  view;
    }
    private void iniitViews() {
        txt_name = view.findViewById(R.id.txt_name);
        txt_name.setText("Waiting Orders");
        edit_date = view.findViewById(R.id.edit_date);
        recy_completed_order = view.findViewById(R.id.recy_completed_order);
        img_back = view.findViewById(R.id.img_back);
        txt_no_orders = view.findViewById(R.id.txt_no_orders);
        GetOrderByRiderId("1");
        //OrderHandler();

        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });


    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new WaitngActivity.DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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


                                    orderAdapter = new Waiting_adapter(getActivity(), orderResponseList);
                                    recy_completed_order.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recy_completed_order.setAdapter(orderAdapter);
                                    txt_no_orders.setVisibility(View.GONE);
                                }else {
                                   // txt_no_orders.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {
                            txt_no_orders.setText("Currently you have no orders");

                            txt_no_orders.setVisibility(View.VISIBLE);
                           // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

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


                                    waiting_adapter = new Waiting_adapter(getActivity(), orderResponseList);
                                    recy_completed_order.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    recy_completed_order.setAdapter(waiting_adapter);

                                }
                            }

                        } else {
                           // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getActivity(), getResources().getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public void onResume() {
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
    public void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    public  void  showProgressDialog(){
        if (progressDialog==null ) {
            progressDialog = new ProgressDialog(getActivity());
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
