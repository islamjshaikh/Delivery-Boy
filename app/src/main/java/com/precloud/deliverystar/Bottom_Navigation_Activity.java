package com.precloud.deliverystar;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.MenuItem;

        import com.google.android.material.bottomnavigation.BottomNavigationView;
        import com.precloud.deliverystar.Activity.EarningsActivity;
        import com.precloud.deliverystar.Activity.OrderActivity;
        import com.precloud.deliverystar.Activity.SettingsActivity;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.navigation.NavController;
        import androidx.navigation.Navigation;
        import androidx.navigation.ui.AppBarConfiguration;
        import androidx.navigation.ui.NavigationUI;

public class Bottom_Navigation_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom__navigation_);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_order:
                        Intent orderintent = new Intent(getApplicationContext(), OrderActivity.class);
                        startActivity(orderintent);
                        break;
                    case R.id.navigation_waiting:

                        break;
                    case R.id.navigation_earning:
                        Intent earningintent = new Intent(getApplicationContext(), EarningsActivity.class);
                        startActivity(earningintent);
                        break;




                }
                return false;
            }

        });
    }
}


