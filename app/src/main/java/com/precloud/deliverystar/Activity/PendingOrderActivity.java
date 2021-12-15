package com.precloud.deliverystar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.precloud.deliverystar.Adapter.OrderAdapter;
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

public class PendingOrderActivity extends AppCompatActivity {
    TextView txt_name;
    public static Date CurrentDate,SelectedDate;
    public static String DOB;
    public static EditText edit_date;
    List<OrderResponse> orderResponseList = new ArrayList<>();
    public static  RecyclerView recy_completed_order;
    OrderAdapter orderAdapter;
      ProgressDialog progressDialog;
    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
        iniitViews();
    }
    private void iniitViews(){
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Pending Orders");
        edit_date = findViewById(R.id.edit_date);
        recy_completed_order = findViewById(R.id.recy_completed_order);
        img_back = findViewById(R.id.img_back);

        GetOrderByRiderId("2");



        edit_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        List<OrderResponse> orderResponseList = new ArrayList<>();
        ProgressDialog progressDialog;
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

            String date = year+"/"+(month+1)+"/"+day;
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "dd/MM/yyyy");

            Date myDate = null;
            try {
                myDate = dateFormat.parse(date);
                Log.e("date", date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");


            DOB = df.format(myDate);
            Date c = Calendar.getInstance().getTime();

            try {
                CurrentDate =   dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()));
                // Log.e("new Date", String.valueOf(CurrentDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SelectedDate = myDate;
            //  Log.e("Selected Date", String.valueOf(SelectedDate));





            edit_date.setText(date);
            GetOrderByDate("2",DOB);
        }

        public void GetOrderByDate(String order_status,String date){
            showProgressDialog();
            DeliveryStarApplication.getmAPIService().getOrderByDate(Storage.getInstance().getString(Constants.Id),order_status,date).enqueue(new Callback<OrderWrapper>() {
                @Override
                public void onResponse(Call<OrderWrapper> call, Response<OrderWrapper> response) {
                    if (response.isSuccessful()){
                        hideProgressDialog();
                        if (response.body()!=null){
                            if (response.body().getStatus().equals("1")){
                                if(response.body().getResult()!=null){
                                    orderResponseList = response.body().getResult();
                                    if (orderResponseList.size()>0 && orderResponseList!=null){


                                        OrderAdapter  orderAdapter = new OrderAdapter(getActivity(),orderResponseList);
                                        recy_completed_order.setLayoutManager(new LinearLayoutManager(getActivity()));
                                        recy_completed_order.setAdapter(orderAdapter);

                                    }
                                }

                            }else {
                               // Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else {
                        hideProgressDialog();
                     //   Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderWrapper> call, Throwable t) {
                    hideProgressDialog();
                    Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();

                }
            });

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


    private void GetOrderByRiderId(String order_status){
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().getOrderByRiderId(Storage.getInstance().getString(Constants.Id),order_status).enqueue(new Callback<OrderWrapper>() {
            @Override
            public void onResponse(Call<OrderWrapper> call, Response<OrderWrapper> response) {
                if (response.isSuccessful()){
                    hideProgressDialog();
                    if (response.body()!=null){
                        if (response.body().getStatus().equals("1")){
                            if(response.body().getResult()!=null){
                                orderResponseList = response.body().getResult();
                                if (orderResponseList.size()>0 && orderResponseList!=null){


                                    orderAdapter = new OrderAdapter(getApplicationContext(),orderResponseList);
                                    recy_completed_order.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    recy_completed_order.setAdapter(orderAdapter);

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
            public void onFailure(Call<OrderWrapper> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();

            }
        });

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

