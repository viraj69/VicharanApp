package com.example.vicharan.Activity.prasangList.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vicharan.R;
import com.example.vicharan.firebase.location.Location;
import com.example.vicharan.firebase.prasang.Prasang;
import com.example.vicharan.generic.UiService;

public class PrasangListItemUi implements UiService {
    private final LocationPrasangPair locationPrasangPair;
    private final AppCompatActivity activity;
    private final OnPrasangClickListener prasangClickListener;
    private final PrasangMediaUi prasangMediaUi;

    private TextView place;
    private TextView sutra;
    private TextView title;

    public PrasangListItemUi(LocationPrasangPair locationPrasangPair, AppCompatActivity activity, OnPrasangClickListener prasangClickListener) {
        this.locationPrasangPair = locationPrasangPair;
        this.activity = activity;
        this.prasangClickListener = prasangClickListener;
        prasangMediaUi = new PrasangMediaUi(locationPrasangPair.getPrasang());
    }

    public LocationPrasangPair getLocationPrasangPair() {
        return locationPrasangPair;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(activity).inflate(R.layout.postlist_item, container, false);
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
        title.setText(locationPrasangPair.getPrasang().getTitle());
        sutra.setText(locationPrasangPair.getPrasang().getSutra());
        place.setText(locationPrasangPair.getLocation().getPlace());
        prasangMediaUi.init();
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

    public static class LocationPrasangPair {
        private final Location location;
        private final Prasang prasang;

        public LocationPrasangPair(Location location, Prasang prasang) {
            this.location = location;
            this.prasang = prasang;
        }

        public Location getLocation() {
            return location;
        }

        public Prasang getPrasang() {
            return prasang;
        }
    }
}
