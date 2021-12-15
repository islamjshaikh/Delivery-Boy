package com.precloud.deliverystar.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.precloud.deliverystar.Activity.ChangeOrderStatus;
import com.precloud.deliverystar.Activity.Home_Menu_Activity;
import com.precloud.deliverystar.Activity.OrderActivity;
import com.precloud.deliverystar.Activity.WaitingOrderDetailActivity;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.Model.UpdateOrderStatusWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Waiting_adapter  extends RecyclerView.Adapter<Waiting_adapter.ViewHolder> {
    Context context;
    List<OrderResponse> arrayList;
    ProgressDialog progressDialog;

    public Waiting_adapter(Context context, List<OrderResponse> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public Waiting_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_accept_order,viewGroup,false);
        return new Waiting_adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Waiting_adapter.ViewHolder viewHolder, int i) {
        final OrderResponse order = arrayList.get(i);
        viewHolder.txt_orderid.setText("Order Id"+" "+order.getOrderId());
        viewHolder.txt_sourceaddress.setText(order.getSourceLocation());
        viewHolder.txt_dest_address.setText(order.getDestinationLocation());
        viewHolder.txt_hotel_name.setText(order.getRestaurantName());
        viewHolder.txt_paymentmod.setText(order.getPaymentMode());
        viewHolder.txt_price.setText(order.getPaymentAmount());
        viewHolder.txt_estimated_time_delivery.setText(order.getOrder_estimated_pickup_time());
        viewHolder.txt_receiptname.setText(order.getRecipientName());
        viewHolder.txt_remark.setText(order.getRemark());


        viewHolder.lnr_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), WaitingOrderDetailActivity.class);
                intent.putExtra("order", (Serializable) order);
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order_Aceept_Reject("2",order.getId(),view.getContext());
            }
        });
        viewHolder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order__Reject("5",order.getId(),view.getContext());
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_remark, txt_estimated_time_delivery, txt_orderid,txt_hotel_name,txt_sourceaddress,txt_dest_address,txt_price,txt_paymentmod,txt_receiptname;
        LinearLayout lnr_order;
        Button btn_accept,btn_reject;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_orderid = (TextView)itemView.findViewById(R.id.txt_orderid);
            txt_hotel_name = (TextView)itemView.findViewById(R.id.txt_hotel_name);
            txt_sourceaddress = (TextView)itemView.findViewById(R.id.txt_sourceaddress);
            txt_dest_address = (TextView)itemView.findViewById(R.id.txt_dest_address);
            txt_price = (TextView)itemView.findViewById(R.id.txt_price);
            txt_estimated_time_delivery = (TextView)itemView.findViewById(R.id.txt_estimated_time_delivery);
            txt_paymentmod = (TextView)itemView.findViewById(R.id.txt_paymentmod);
            lnr_order = itemView.findViewById(R.id.lnr_order);
            txt_receiptname = itemView.findViewById(R.id.txt_receiptname);
            txt_remark = itemView.findViewById(R.id.txt_remark);
            btn_accept = itemView.findViewById(R.id.btn_accept);
            btn_reject = itemView.findViewById(R.id.btn_reject);
        }
    }

    private void Order_Aceept_Reject(String status, String orderid,final Context context1){
        showProgressDialog(context1);
        DeliveryStarApplication.getmAPIService().acceptOrderStatus(Storage.getInstance().getString(Constants.Id), orderid,status,"","").enqueue(
                new Callback<UpdateOrderStatusWrapper>() {
                    @Override
                    public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                        if (response.isSuccessful()){
                            hideProgressDialog();
                            if(response.body()!=null){
                                if(response.body().getStatus().equals("1")) {
                                   // Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //
                                    showAlert(response.body().getMessage());

                                }

                            }
                        }else {
                            hideProgressDialog();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void showAlert(String message) {
        // 1. Instantiate an <code><a href="/reference/android/app/AlertDialog.Builder.html">AlertDialog.Builder</a></code> with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(context, Home_Menu_Activity.class);
                        intent.putExtra("flag","Accept");
                        context.startActivity(intent);
                    }
                });

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message)
                .setTitle("Order Staus");

// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void Order__Reject(String status, String orderid,final Context context1){
        showProgressDialog(context1);
        DeliveryStarApplication.getmAPIService().acceptOrderStatus(Storage.getInstance().getString(Constants.Id), orderid,status,"","").enqueue(
                new Callback<UpdateOrderStatusWrapper>() {
                    @Override
                    public void onResponse(Call<UpdateOrderStatusWrapper> call, Response<UpdateOrderStatusWrapper> response) {
                        if (response.isSuccessful()){
                            hideProgressDialog();
                            if(response.body()!=null){
                                if(response.body().getStatus().equals("1")) {
                                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context1, Home_Menu_Activity.class);
                                    intent.putExtra("flag","Reject");
                                    context1.startActivity(intent);
                                }

                            }
                        }else {
                            hideProgressDialog();
                            Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateOrderStatusWrapper> call, Throwable t) {
                        hideProgressDialog();
                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }


    public  void  showProgressDialog(Context context){
        if (progressDialog==null ) {
            progressDialog = new ProgressDialog(context);
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