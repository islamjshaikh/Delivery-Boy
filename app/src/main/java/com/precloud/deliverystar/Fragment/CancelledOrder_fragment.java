package com.precloud.deliverystar.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.precloud.deliverystar.Adapter.CancelledOrderAdapter;
import com.precloud.deliverystar.Adapter.OrderAdapter;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.Model.OrderWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;


public class CancelledOrder_fragment extends Fragment {

    List<OrderResponse> orderResponseList = new ArrayList<>();
    RecyclerView rec_cancelled_order;
    CancelledOrderAdapter orderAdapter;
   ProgressDialog progressDialog;
   TextView txt_no_orders;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cancelled_order_fragment,container,false);
        rec_cancelled_order = view.findViewById(R.id.rec_cancelled_order);
        txt_no_orders = view.findViewById(R.id.txt_no_orders);
        GetOrderByRiderId("5");
        return  view;
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


                                    orderAdapter = new CancelledOrderAdapter(getActivity(),orderResponseList);
                                    rec_cancelled_order.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    rec_cancelled_order.setAdapter(orderAdapter);

                                }
                            }

                        }else {
                            txt_no_orders.setVisibility(View.VISIBLE);
                           // Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                }else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
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
