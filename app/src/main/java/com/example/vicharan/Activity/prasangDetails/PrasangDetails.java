package com.example.vicharan.Activity.prasangDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.chahinem.pageindicator.PageIndicator;
import com.example.vicharan.Activity.prasangDetails.adapter.ViewPagerAdapter;
import com.example.vicharan.R;
import com.example.vicharan.firebase.prasang.DbPrasang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class PrasangDetails extends AppCompatActivity {
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private ArrayList<DbPrasang.LocationPrasangPair> locationPrasangPairList;

    public static void startActivity(Activity caller, LinkedList<DbPrasang.LocationPrasangPair> locationPrasangPairList) {
        Intent i = new Intent(caller, PrasangDetails.class);
        i.putExtra("key", locationPrasangPairList);
        caller.startActivity(i);
    }

    private void parseIntent() {
        locationPrasangPairList = (ArrayList<DbPrasang.LocationPrasangPair>) getIntent().getSerializableExtra("key");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prasang_details);
        parseIntent();
        viewPager = findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        Collections.shuffle(locationPrasangPairList);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), locationPrasangPairList);
        viewPager.setAdapter(pagerAdapter);
    }
}