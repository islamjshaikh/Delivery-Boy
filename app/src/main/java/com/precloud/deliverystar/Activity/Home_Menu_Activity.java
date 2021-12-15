package com.precloud.deliverystar.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.precloud.deliverystar.Fragment.About_fragment;
import com.precloud.deliverystar.Fragment.CancelledOrder_fragment;
import com.precloud.deliverystar.Fragment.Completed_Order_Fragment;
import com.precloud.deliverystar.Fragment.Pending_Order_Fragment;
import com.precloud.deliverystar.Fragment.ProfileFragment;
import com.precloud.deliverystar.Fragment.RiderStatus_Fragment;
import com.precloud.deliverystar.Fragment.SettingFragment;
import com.precloud.deliverystar.Fragment.Waiting_Order_Fragment;
import com.precloud.deliverystar.Model.GetUserProfileResponse;
import com.precloud.deliverystar.Model.GetUserProfileWrapper;
import com.precloud.deliverystar.R;
import com.precloud.deliverystar.Utility.Constants;
import com.precloud.deliverystar.Utility.DeliveryStarApplication;
import com.precloud.deliverystar.Utility.Storage;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home_Menu_Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    CircleImageView image_user;
    TextView txt_name,txt_mobile;
    ProgressDialog progressDialog;
    Toolbar toolbar;
    List<GetUserProfileResponse> getUserProfileResponseList = new ArrayList<>();
    String flag="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__menu_);
         toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);

        image_user = header.findViewById(R.id.image_user);
        txt_mobile = header.findViewById(R.id.txt_mobile);
        txt_name = header.findViewById(R.id.txt_name);
        getUserProfilleCall();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        if (getIntent().getExtras()!=null){
            flag = getIntent().getExtras().getString("flag");


        }

       /* NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/
       if(flag!=null) {
           if (flag.equals("")) {
               toolbar.setTitle("New Orders");
               displaySelectedScreen(R.id.navigation_waiting);
           } else {
               if (flag.equals("Accept")) {
                   displaySelectedScreen(R.id.navigation_order);
                   toolbar.setTitle("Pending Orders");
               } else {
                   if (flag.equals("Reject")) {
                       displaySelectedScreen(R.id.navigation_cancelled);
                       toolbar.setTitle("Cancelled Orders");
                   }
               }
           }
       }else {
           toolbar.setTitle("New Orders");
           displaySelectedScreen(R.id.navigation_waiting);
       }

        navigationView.bringToFront();
       navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               displaySelectedScreen(item.getItemId());
               return true;
           }
       });
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home__menu_, menu);
        return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.navigation_waiting:
                toolbar.setTitle("New Orders");
                fragment = new Waiting_Order_Fragment();
                break;
            case R.id.navigation_order:
                toolbar.setTitle("Pending Orders");

                fragment = new Pending_Order_Fragment();
                break;
            case R.id.navigation_completed:
                toolbar.setTitle("Completed Orders");
                fragment = new Completed_Order_Fragment();
                break;
            case R.id.navigation_cancelled:
                toolbar.setTitle("Cancelled Orders");
                fragment = new CancelledOrder_fragment();
                break;
            case  R.id.navigation_riderstatus:
                toolbar.setTitle("Rider Status");
                fragment = new RiderStatus_Fragment();
                break;
            case R.id.nav_about:
                toolbar.setTitle("About");

                fragment = new About_fragment();
                break;

            case R.id.navigation_profile:
                toolbar.setTitle("My Profile");
                fragment = new ProfileFragment();
                break;



            case R.id.nav_share_app:

                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                String applink= "Delivery Star";
                i.putExtra(Intent.EXTRA_TEXT, applink);
                 startActivity(Intent.createChooser(i, "choose one"));

              break;
            case R.id.nav_logout:
                Storage.getInstance().removeKey(Constants.Id);
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
                            txt_name.setText(getUserProfileResponseList.get(0).getName());
                            txt_mobile.setText(getUserProfileResponseList.get(0).getPhone());
                            Glide.with(getApplicationContext()).load(getUserProfileResponseList.get(0).getImage()).into(image_user);

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
}
