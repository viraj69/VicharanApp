package com.example.vicharan.Fragments.map;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.vicharan.Fragments.LocationFragment;
import com.example.vicharan.Fragments.map.ui.Ui;
import com.example.vicharan.R;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

import java.util.HashMap;
import java.util.List;

public class MapFragment extends LocationFragment implements Ui.UiListener, LocationCallbackWrapper.LocationCallbackListener {
    private final Ui ui = new Ui(this, this);

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDeFuuo_ueSXMlCCQQLUIFgFAs4Xo3ULNg");
        super.onViewCreated(view, savedInstanceState);
        //PlacesClient placesClient = Places.createClient(getActivity());
        ui.onViewCreated(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @SuppressLint("MissingPermission")
    private void createFusedLocationClient() {
        if (mFusedLocationClient != null) {
            return;
        }
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationCallbackWrapper locationCallbackWrapper = new LocationCallbackWrapper(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallbackWrapper, Looper.myLooper());
    }

    @Override
    public void onStart() {
        super.onStart();
        DbPrasang.getAllWithLocation((List<DbPrasang.LocationPrasangPair> locationPrasangPairList) -> {

            HashMap<com.example.vicharan.firebase.location.Location, Integer> locationsPrasangCountHashmap = new HashMap<>();

            for (DbPrasang.LocationPrasangPair locationPrasangPair : locationPrasangPairList) {
                Integer prasangCount = locationsPrasangCountHashmap.get(locationPrasangPair.getLocation());
                if (prasangCount == null) {
                    prasangCount = 1;
                } else {
                    prasangCount++;
                }
                locationsPrasangCountHashmap.put(locationPrasangPair.getLocation(), prasangCount);
            }
            ui.getGoogleMapUi().putMarkers(locationsPrasangCountHashmap);
        });
    }

    @Override
    public void onLocationAvailable(Location location) {

    }


    @Override
    public void onPlaceSelectedWithGoogleAutoComplete(@NonNull Place place) {
        ui.getGoogleMapUi().focusOnMap(place.getLatLng());
    }

    @Override
    protected void onLocationPermissionAvailable() {
        ui.getGoogleMapUi().onLocationEnabled();
        createFusedLocationClient();
    }
}