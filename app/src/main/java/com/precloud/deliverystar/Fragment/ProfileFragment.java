package com.precloud.deliverystar.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.precloud.deliverystar.Activity.SettingsActivity;
import com.precloud.deliverystar.Model.GetUserProfileResponse;
import com.precloud.deliverystar.Model.GetUserProfileWrapper;
import com.precloud.deliverystar.Model.ProfileUpdateWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    TextView txt_name;
    EditText edit_name,edit_email,edit_address,edit_phone,edit_starttime,edit_endtime;
    List<GetUserProfileResponse> getUserProfileResponseList = new ArrayList<>();
    private ProgressDialog progressDialog;
    Button btn_update;
    ImageView img_edit,img_back;
    View view;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_profile,container,false);
         initViews();
        return view;
    }

    private void initViews(){


        edit_name = view.findViewById(R.id.edit_name);
        edit_email = view.findViewById(R.id.edit_email);
        edit_address = view.findViewById(R.id.edit_address);
        edit_phone =view. findViewById(R.id.edit_phone);
        edit_starttime = view.findViewById(R.id.edit_starttime);
        edit_endtime = view.findViewById(R.id.edit_endtime);
        btn_update = view.findViewById(R.id.btn_update);
        img_edit = view.findViewById(R.id.img_edit);
        checkenable(false);
        getUserProfilleCall();


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


                        Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();

                    }

                }else {
                    hideProgressDialog();

                    Toast.makeText(getActivity(),getResources().getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileUpdateWrapper> call, Throwable t) {
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
