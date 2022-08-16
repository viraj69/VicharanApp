package com.example.vicharan.Activity.prasangList.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vicharan.GlobalApplication;
import com.example.vicharan.R;
import com.example.vicharan.firebase.location.GenericLocation;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.example.vicharan.firebase.prasang.GenericPrasang;
import com.example.vicharan.generic.UiService;

public class PrasangListItemUi implements UiService {
    private final DbPrasang.LocationPrasangPair locationPrasangPair;
    private final AppCompatActivity activity;
    private final OnPrasangClickListener prasangClickListener;
    private final PrasangMediaUi prasangMediaUi;

    private TextView place;
    private TextView sutra;
    private TextView title;

    public PrasangListItemUi(DbPrasang.LocationPrasangPair locationPrasangPair, AppCompatActivity activity, OnPrasangClickListener prasangClickListener) {
        this.locationPrasangPair = locationPrasangPair;
        this.activity = activity;
        this.prasangClickListener = prasangClickListener;
        prasangMediaUi = new PrasangMediaUi(locationPrasangPair.getPrasang());
    }

    public DbPrasang.LocationPrasangPair getLocationPrasangPair() {
        return locationPrasangPair;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(activity).inflate(R.layout.prasanglist_item, container, false);
    }

    @Override
    public void onViewCreated(View v) {
        place = v.findViewById(R.id.postlist_place);
        sutra = v.findViewById(R.id.postlist_sutra);
        title = v.findViewById(R.id.postlist_title);
        prasangMediaUi.onViewCreated(v);
        v.setOnClickListener(this);
    }

    @Override
    public void init() {
        boolean isGujaratiLanguageSelected = GlobalApplication.getInstance().isGujaratiLanguageSelected();
        if (isGujaratiLanguageSelected) {
            loadPrasang(locationPrasangPair.getPrasang().getGujaratiVersion());
            loadLocation(locationPrasangPair.getLocation().getGujaratiVersion());
        } else {
            loadPrasang(locationPrasangPair.getPrasang().getEnglishVersion());
            loadLocation(locationPrasangPair.getLocation().getEnglishVersion());
        }
        prasangMediaUi.init();
    }

    private void loadPrasang(GenericPrasang prasang) {
        title.setText(prasang.getTitle());
        sutra.setText(prasang.getSutra());
    }

    private void loadLocation(GenericLocation location) {
        place.setText(location.getPlace());
    }

    @Override
    public void onViewDestroyed() {
        prasangMediaUi.onViewDestroyed();
    }

    @Override
    public void onClick(View view) {
        prasangClickListener.onPrasangClick(this);
    }

    public interface OnPrasangClickListener {
        void onPrasangClick(PrasangListItemUi prasangListItemUi);
    }

}
