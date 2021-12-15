package com.precloud.deliverystar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.precloud.deliverystar.Model.GetUserProfileResponse;
import com.precloud.deliverystar.Model.GetUserProfileWrapper;
import com.precloud.deliverystar.Model.ProfileUpdateWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    TextView  txt_name;
    EditText edit_name,edit_email,edit_address,edit_phone,edit_starttime,edit_endtime;
    List<GetUserProfileResponse> getUserProfileResponseList = new ArrayList<>();
    private ProgressDialog progressDialog;
    Button btn_update;
    ImageView img_edit,img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
    }

    private void initViews(){
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Profile");

        edit_name = findViewById(R.id.edit_name);
        edit_email = findViewById(R.id.edit_email);
        edit_address = findViewById(R.id.edit_address);
        edit_phone = findViewById(R.id.edit_phone);
        edit_starttime = findViewById(R.id.edit_starttime);
        edit_endtime = findViewById(R.id.edit_endtime);
        btn_update = findViewById(R.id.btn_update);
        img_edit = findViewById(R.id.img_edit);
        img_back = findViewById(R.id.img_back);
        checkenable(false);
        getUserProfilleCall();

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkenable(true);

            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdareUserProfileCall();
            }
        });

    }
    private void checkenable(Boolean isenable){
        edit_name.setEnabled(isenable);
        edit_email.setEnabled(isenable);
        edit_address.setEnabled(isenable);
        edit_starttime.setEnabled(isenable);
        edit_endtime.setEnabled(isenable);


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
                            edit_name.setText(getUserProfileResponseList.get(0).getName());
                            edit_email.setText(getUserProfileResponseList.get(0).getEmail());
                            edit_address.setText(getUserProfileResponseList.get(0).getAddress());
                            edit_phone.setText(getUserProfileResponseList.get(0).getPhone());
                            edit_starttime.setText(getUserProfileResponseList.get(0).getStartTime());
                            edit_endtime.setText(getUserProfileResponseList.get(0).getEndTime());
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

    private void UpdareUserProfileCall(){
        showProgressDialog();

        DeliveryStarApplication.getmAPIService().updateProfile(Storage.getInstance().getString(Constants.Id),
             edit_name.getText().toString().trim(),
             edit_email.getText().toString().trim(),
             edit_address.getText().toString().trim()).enqueue(new Callback<ProfileUpdateWrapper>() {
         @Override
         public void onResponse(Call<ProfileUpdateWrapper> call, Response<ProfileUpdateWrapper> response) {
             if (response.isSuccessful()){
                 hideProgressDialog();

                 if (response.body()!=null){


                     Toast.makeText(getApplicationContext(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                 }

             }else {
                 hideProgressDialog();

                 Toast.makeText(getApplicationContext(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onFailure(Call<ProfileUpdateWrapper> call, Throwable t) {
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
