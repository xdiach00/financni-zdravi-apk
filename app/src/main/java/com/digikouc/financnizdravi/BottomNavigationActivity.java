package com.digikouc.financnizdravi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;


public class BottomNavigationActivity extends AppCompatActivity {
    float x1, x2, y1, y2;
    LoginResponse loginResponse;
    Bundle bundle = new Bundle();
    String email;
    String api_token;
    Fragment DashboardFragment, EnterDataFragment, ConnectAccountFragment, SettingsFragment;

    @Override
    public void onBackPressed() {
        tellFragments();
        super.onBackPressed();
    }

    private void tellFragments(){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for(Fragment f : fragments){
            if(f != null && f instanceof DashboardFragment)
                ((DashboardFragment)f).onBackPressed();
            if(f != null && f instanceof SettingsFragment)
                ((SettingsFragment)f).onBackPressed();
            if(f != null && f instanceof ConnectAccountFragment)
                ((ConnectAccountFragment)f).onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        View logout = findViewById(R.id.imageLogout);
        Intent intent = getIntent();
        RequestQueue queue = Volley.newRequestQueue(this);
        DashboardFragment = new DashboardFragment();
        EnterDataFragment = new EnterDataFragment();
        ConnectAccountFragment = new ConnectAccountFragment();
        SettingsFragment = new SettingsFragment();

        String url ="https://financni-zdravi.cz/api/user/logout=";
        email = intent.getStringExtra("EMAIL");


        if(intent.getExtras() != null){
            api_token = intent.getStringExtra("TOKEN");
            bundle.putString("TOKEN", api_token);
            bundle.putString("EMAIL", email);

            Log.e("TAG", "=====> "+api_token);
        }

        DashboardFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, DashboardFragment).commit();



        logout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String url_logout = url + api_token;
                StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url_logout,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                Log.e("TAG", "=====>" + response);
                                Intent intent = new Intent(BottomNavigationActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                }
                           }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("TAG", "=====>" + "ERROR");
                            }
                        });
                queue.add(stringRequest);
                return false;
            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

            switch (item.getItemId()){
                case R.id.navigation_dashboard:
                    selectedFragment = DashboardFragment;
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.navigation_enter_data:
                    selectedFragment = EnterDataFragment;
                    selectedFragment.setArguments(bundle);
                    break;
                case R.id.navigation_connect_account:
                    selectedFragment = ConnectAccountFragment;
                    break;
                case R.id.navigation_settings:
                    selectedFragment = SettingsFragment;
                    selectedFragment.setArguments(bundle);
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

            return true;
        }
    };



}