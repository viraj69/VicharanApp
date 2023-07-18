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

    static String tag;

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
        Bundle args = new Bundle();
        args.putString("Key", tag);
        map.setArguments(args);
        fm.beginTransaction().add(R.id.frame, map, "1").replace(R.id.frame, map).commit();
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

//        runScriptForBlueColorDescription();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }

//    private void runScriptForBlueColorDescription() {
//        DbPrasang.getAllWithLocation(locationPrasangPairs -> {
//            for (DbPrasang.LocationPrasangPair locationPrasangPair : locationPrasangPairs) {
//                Prasang prasang = locationPrasangPair.getPrasang();
//
//                String engDes = prasang.getEnglishVersion().getDescription();
//                if (!engDes.contains("html")) {
//                    engDes = "<html>" + "<head>" + "<style>" + "@font-face { font-family: europa_regular;src: url('https://firebasestorage.googleapis.com/v0/b/vicharan-app.appspot.com/o/font%2Feuropa_regular.ttf?alt=media');} body {font-family: europa_regular, serif;}" + "</style>" + "</head>" + "<body>" + "<span style='color:#4981a3'>" + engDes + "</span>" + "</body>" + "</html>";
//                    prasang.getEnglishVersion().setDescription(engDes);
//                    DbPrasang.update(prasang, (unused) -> {
//                        System.out.println("Updated: " + prasang.getId());
//                    });
//                }
//            }
//        });
//    }
}