package com.precloud.deliverystar.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.precloud.deliverystar.Activity.CompletedOrdersActivity;
import com.precloud.deliverystar.Activity.LoginActivity;
import com.precloud.deliverystar.Activity.OnlineStatusActivity;
import com.precloud.deliverystar.Activity.OrderActivity;
import com.precloud.deliverystar.Activity.PendingOrderActivity;
import com.precloud.deliverystar.Activity.ProfileActivity;
import com.precloud.deliverystar.Activity.SettingsActivity;
import com.precloud.deliverystar.Activity.WaitngActivity;
import com.precloud.deliverystar.Model.GetUserProfileResponse;
import com.precloud.deliverystar.Model.GetUserProfileWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;


public class SettingFragment extends Fragment {
    TextView txt_name,txt_username,txt_mob;
    private ProgressDialog progressDialog;
    List<GetUserProfileResponse> getUserProfileResponseList = new ArrayList<>();
    LinearLayout lnr_edit,lnr_driverearning,lnr_completed_order,lnr_logout,lnr_chk_status,lnr_pendingorder;
    ImageView img_profie;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         view = inflater.inflate(R.layout.activity_settings,container,false);
        initViews();
        return view;
    }


    private void initViews(){

        txt_username = view.findViewById(R.id.txt_username);
        txt_mob = view.findViewById(R.id.txt_mob);
        lnr_edit = view.findViewById(R.id.lnr_edit);
        lnr_pendingorder = view.findViewById(R.id.lnr_pendingorder);
        lnr_completed_order = view.findViewById(R.id.lnr_completed_order);
        lnr_chk_status = view.findViewById(R.id.lnr_chk_status);
        lnr_logout = view.findViewById(R.id.lnr_logout);
        img_profie = view.findViewById(R.id.img_profie);

        getUserProfilleCall();



        lnr_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);

            }
        });

        lnr_completed_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CompletedOrdersActivity.class);
                startActivity(intent);

            }
        });


        lnr_chk_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OnlineStatusActivity.class);
                startActivity(intent);



            }
        });

        lnr_pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PendingOrderActivity.class);
                startActivity(intent);

            }
        });


        lnr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Storage.getInstance().removeKey(Constants.Id);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });



    }
    private void getUserProfilleCall(){
        showProgressDialog();
        DeliveryStarApplication.getmAPIService().getUserProfile(Storage.getInstance().getString(Constants.Id)).enqueue(new Callback<GetUserProfileWrapper>() {
            @Override
            public void onResponse(Call<GetUserProfileWrapper> call, Response<GetUserProfileWrapper> response) {
                if (response.isSuccessful()){
                    hideProgressDialog();
                    GetUserProfileWrapper model = response.body();
                    if (model.getStatus().equals("1")){
                        if (model.getResult()!=null){
                            getUserProfileResponseList = model.getResult();
                            txt_username.setText(getUserProfileResponseList.get(0).getName());
                            txt_mob.setText(getUserProfileResponseList.get(0).getPhone());
                            Glide.with(getActivity()).load(getUserProfileResponseList.get(0).getImage()).into(img_profie);

                        }

                    }else {
                        Toast.makeText(getActivity(),model.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }else {
                    hideProgressDialog();
                    Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserProfileWrapper> call, Throwable t) {
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
