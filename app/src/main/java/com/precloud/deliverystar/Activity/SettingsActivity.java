package com.precloud.deliverystar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.precloud.deliverystar.Model.GetUserProfileResponse;
import com.precloud.deliverystar.Model.GetUserProfileWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Service.TrackerMapActivity;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {
  TextView txt_name,txt_username,txt_mob;
    private ProgressDialog progressDialog;
    List<GetUserProfileResponse> getUserProfileResponseList = new ArrayList<>();
    LinearLayout lnr_edit,lnr_driverearning,lnr_completed_order,lnr_logout,lnr_chk_status,lnr_pendingorder;
     ImageView img_profie,img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initViews();
    }

    private void initViews(){

        txt_username = findViewById(R.id.txt_username);
        txt_mob = findViewById(R.id.txt_mob);
        lnr_edit = findViewById(R.id.lnr_edit);
        lnr_pendingorder = findViewById(R.id.lnr_pendingorder);
        lnr_completed_order = findViewById(R.id.lnr_completed_order);
        lnr_chk_status = findViewById(R.id.lnr_chk_status);
        lnr_logout = findViewById(R.id.lnr_logout);
        img_profie = findViewById(R.id.img_profie);

      getUserProfilleCall();

      img_back.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              finish();
          }
      });

        lnr_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lnr_completed_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),CompletedOrdersActivity.class);
                startActivity(intent);
                finish();
            }
        });


        lnr_chk_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), OnlineStatusActivity.class);
                startActivity(intent);
                finish();


            }
        });

        lnr_pendingorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PendingOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });


        lnr_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Storage.getInstance().removeKey(Constants.Id);
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
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
                            Glide.with(getApplicationContext()).load(getUserProfileResponseList.get(0).getImage()).into(img_profie);

                        }

                    }else {
                        Toast.makeText(getApplicationContext(),model.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                }else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetUserProfileWrapper> call, Throwable t) {
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

    private void removeColor(BottomNavigationView view) {
        for (int i = 0; i < view.getMenu().size(); i++) {
            MenuItem item = view.getMenu().getItem(i);
            item.setChecked(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),OrderActivity.class);
        startActivity(intent);
        finish();
    }
}
