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
import com.example.vicharan.R;
import com.example.vicharan.firebase.Apartment.DbApartment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;

public class MapFragment extends LocationFragment implements Ui.UiListener, LocationCallbackWrapper.LocationCallbackListener {
    private static final String DefaultCountryName = "Canada";

    private final Ui ui = new Ui(this, this);

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDeFuuo_ueSXMlCCQQLUIFgFAs4Xo3ULNg");
        //PlacesClient placesClient = Places.createClient(getActivity());
        ui.onViewCreated(view);
    }

    private void fetchDbApartments(String country) {
        ui.clearGoogleMap();
        if (country == null) {
            country = DefaultCountryName;
        }
        DbApartment.getByCountryName(country, ui::putApartmentMarkers);
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
    public void onGoogleMapReady(GoogleMap googleMap) {
        if (hasLocationPermission()) {
            createFusedLocationClient();
        }
    }

    @Override
    public void onLocationAvailable(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        ui.focusOnMap(latLng);
        fetchDbApartments(null);
    }


    @Override
    public void onPlaceSelectedWithGoogleAutoComplete(@NonNull Place place) {
        ui.focusOnMap(place.getLatLng());
    }

    @Override
    protected void onLocationPermissionAvailable() {
        ui.onLocationEnabled();
        createFusedLocationClient();
    }
}