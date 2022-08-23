package com.example.vicharan.Activity.prasangDetails;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.vicharan.Activity.MainActivity;
import com.example.vicharan.Activity.prasangDetails.adapter.ViewPagerAdapter;
import com.example.vicharan.Fragments.map.MapFragment;
import com.example.vicharan.R;
import com.example.vicharan.firebase.prasang.DbPrasang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class PrasangDetails extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private FragmentPagerAdapter pagerAdapter;
    private ArrayList<DbPrasang.LocationPrasangPair> locationPrasangPairList;
    ImageView next, previous,back;


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

        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);

        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (viewPager.getCurrentItem() < pagerAdapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                break;
            case R.id.previous:
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                break;
        }
    }
}