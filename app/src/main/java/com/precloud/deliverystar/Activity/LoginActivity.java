package com.precloud.deliverystar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.precloud.deliverystar.Bottom_Navigation_Activity;
import com.precloud.deliverystar.Model.LoginResponse;
import com.precloud.deliverystar.Model.LoginWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
 Button btn_login;
 TextView txt_name;
 ImageView img_back;
    EditText edit_mobile_number,edit_password;
    List<LoginResponse> loginResponse = new ArrayList<>();
    private ProgressDialog progressDialog;
    TextView forgetpassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews(){
        btn_login = findViewById(R.id.btn_login);
        img_back = findViewById(R.id.img_back);
        txt_name = findViewById(R.id.txt_name);
        txt_name.setText("Delivery Star");
        img_back.setVisibility(View.GONE);
        edit_mobile_number = findViewById(R.id.edt_mobile);
        edit_password = findViewById(R.id.edt_password);
        forgetpassword = findViewById(R.id.forgetpassword);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edit_mobile_number.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter mobile number",Toast.LENGTH_SHORT).show();
                    edit_mobile_number.requestFocus();
                }else {
                    if (edit_password.getText().toString().equals("")){
                        Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                        edit_password.requestFocus();

                    }else {
                        logincall();
                    }
                }
            }

        });

        forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ForgotPwdActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void logincall(){
        showProgressDialog();
     Log.e("logintoken",Storage.getInstance().getString(Constants.TOKEN));
        DeliveryStarApplication.getmAPIService().login(edit_mobile_number.getText().toString().trim(),edit_password.getText().toString().trim(),Storage.getInstance().getString(Constants.TOKEN),"A").enqueue(new Callback<LoginWrapper>() {
            @Override
            public void onResponse(Call<LoginWrapper> call, Response<LoginWrapper> response) {
                if (response.isSuccessful()){
                    hideProgressDialog();
                    if(response.body()!=null){
                        LoginWrapper model = response.body();
                        Log.e("res login", String.valueOf(response.body()));
                        if (model!=null){
                            if(model.getStatus().equals("1")){
                                loginResponse = model.getResult();
                                Log.e("id",loginResponse.get(0).getId());
                                Log.e("name",loginResponse.get(0).getName());
                                Log.e("email",loginResponse.get(0).getEmail());

                                Storage.getInstance().setString(Constants.Id,loginResponse.get(0).getId());

                                Intent intent = new Intent(getApplicationContext(),Home_Menu_Activity.class);
                                startActivity(intent);
                                finish();

                            }else {
                                if (model.getStatus().equals("0")){
                                    Toast.makeText(getApplicationContext(),model.getMessage(),Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    }

                }else {
                    hideProgressDialog();
                    try {
                        Log.e("error in login res",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginWrapper> call, Throwable t) {
                hideProgressDialog();
                // Log.e("error",t.getLocalizedMessage());
                Toast.makeText(getApplicationContext(),"Something went wrong"+t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

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
