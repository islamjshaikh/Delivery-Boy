package com.precloud.deliverystar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.precloud.deliverystar.Activity.CompletedOrderDetailActivity;
import com.precloud.deliverystar.Model.OrderResponse;
import com.precloud.deliverystar.R;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CompletedOrderAdapter extends RecyclerView.Adapter<CompletedOrderAdapter.ViewHolder> {
    Context context;
    List<OrderResponse> arrayList;
    public CompletedOrderAdapter(Context context, List<OrderResponse> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CompletedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_completed_orders,viewGroup,false);
        return new CompletedOrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CompletedOrderAdapter.ViewHolder viewHolder, int i) {
        final OrderResponse order = arrayList.get(i);
        viewHolder.txt_orderid.setText("#Order"+" "+order.getOrderId());
        viewHolder.txt_sourceaddress.setText(order.getSourceLocation());
        viewHolder.txt_dest_address.setText(order.getDestinationLocation());
        viewHolder.txt_hotel_name.setText(order.getRestaurantName());
        viewHolder.txt_paymentmod.setText(order.getPaymentMode());
        viewHolder.pick_up_time.setText(order.getOrder_estimated_pickup_time());
        if(order.getPaymentAmount()!=null && !order.getPaymentAmount().equals("")) {

            viewHolder.txt_price.setText( order.getPaymentAmount());
        }

        viewHolder.txt_receiptname.setText(order.getRecipientName());
        viewHolder.txt_remark.setText(order.getRemark());


        viewHolder.lnr_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CompletedOrderDetailActivity.class);
                intent.putExtra("order", (Serializable) order);
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_orderid,txt_remark, txt_hotel_name,txt_sourceaddress,txt_dest_address,txt_price,txt_paymentmod,txt_receiptname,pick_up_time;
        LinearLayout lnr_order;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_orderid = (TextView)itemView.findViewById(R.id.txt_orderid);
            txt_hotel_name = (TextView)itemView.findViewById(R.id.txt_hotel_name);
            txt_sourceaddress = (TextView)itemView.findViewById(R.id.txt_sourceaddress);
            txt_dest_address = (TextView)itemView.findViewById(R.id.txt_dest_address);
            txt_price = (TextView)itemView.findViewById(R.id.txt_price);
            txt_paymentmod = (TextView)itemView.findViewById(R.id.txt_paymentmod);
            lnr_order = itemView.findViewById(R.id.lnr_order);
            txt_receiptname = itemView.findViewById(R.id.txt_receiptname);
            pick_up_time = itemView.findViewById(R.id.pick_up_time);
            txt_remark = itemView.findViewById(R.id.txt_remark);
        }
    }
}