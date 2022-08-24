package com.example.aksharSparsh.Activity.prasangDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.aksharSparsh.Activity.prasangDetails.adapter.ViewPagerAdapter;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class PrasangDetails extends AppCompatActivity implements View.OnClickListener {
    private ImageView next, previous;
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

    private void hideToolbarTitle() {

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        mTitle.setText("");
        setSupportActionBar(toolbarTop);
        ((TextView) toolbarTop.findViewById(R.id.toolbar_title)).setText("");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowTitleEnabled(false);
        ab.setHomeAsUpIndicator(R.drawable.ic_action_name);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setTitle("");
//        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText("");
    }

    private void setupFragmentViewPageListener() {
        viewPager = findViewById(R.id.view_pager);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        Collections.shuffle(locationPrasangPairList);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), locationPrasangPairList);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                handleNextAndPreviousBtnVisibility();
            }
        });
    }

    private void setupPreviousAndNextBtn() {
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        handleNextAndPreviousBtnVisibility();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prasang_details);
        parseIntent();
        hideToolbarTitle();
        setupFragmentViewPageListener();
        setupPreviousAndNextBtn();
    }

    private void toggleNextButtonVisibility(boolean show) {
        next.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void togglePreviousButtonVisibility(boolean show) {
        previous.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void handleNextAndPreviousBtnVisibility() {
        if (viewPager.getCurrentItem() == pagerAdapter.getCount() - 1) {
            toggleNextButtonVisibility(false);
        } else {
            toggleNextButtonVisibility(true);
        }
        if (viewPager.getCurrentItem() == 0) {
            togglePreviousButtonVisibility(false);
        } else {
            togglePreviousButtonVisibility(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                if (viewPager.getCurrentItem() < pagerAdapter.getCount()) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
                handleNextAndPreviousBtnVisibility();
                break;
            case R.id.previous:
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
                handleNextAndPreviousBtnVisibility();
                break;
        }
    }
}