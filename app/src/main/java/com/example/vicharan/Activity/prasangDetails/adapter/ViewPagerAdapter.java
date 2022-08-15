package com.example.vicharan.Activity.prasangDetails.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.vicharan.Activity.prasangDetails.ViewPagerFragment;
import com.example.vicharan.firebase.prasang.DbPrasang;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<DbPrasang.LocationPrasangPair> locationPrasangPairList;

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, List<DbPrasang.LocationPrasangPair> prasangList) {
        super(fragmentManager);
        this.locationPrasangPairList = prasangList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setData(locationPrasangPairList.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return locationPrasangPairList.size();
    }
}
