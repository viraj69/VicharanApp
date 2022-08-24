package com.example.aksharSparsh.Activity;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.example.aksharSparsh.Fragments.MyAccountFragment;
import com.example.aksharSparsh.Fragments.ProfileFragment;
import com.example.aksharSparsh.Fragments.map.MapFragment;
import com.example.aksharSparsh.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    final Fragment profile = new ProfileFragment();
    final Fragment map = new MapFragment();
    final Fragment account = new MyAccountFragment();
    BottomNavigationView bottomNavigationView;
    Fragment active = map;
    private FirebaseUser curUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.nav_view);

        FirebaseFirestore.setLoggingEnabled(true);

        auth = FirebaseAuth.getInstance();

        final FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.frame, profile, "3").hide(profile).commit();
        fm.beginTransaction().add(R.id.frame, account, "4").hide(account).commit();
        fm.beginTransaction().add(R.id.frame, map, "1").commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                if (item.getItemId() == R.id.navigation_map) {

                    fm.beginTransaction().hide(active).show(map).commit();
                    active = map;
                } else if (item.getItemId() == R.id.navigation_profile) {
                    curUser = auth.getCurrentUser();
                    if (curUser != null) {
                        fm.beginTransaction().hide(active).show(account).disallowAddToBackStack().commit();
                        active = account;
                    } else {
                        fm.beginTransaction().hide(active).show(profile).disallowAddToBackStack().commit();
                        active = profile;
                    }


                }
                return true;
            }
        });
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_map);
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}