package com.example.aksharSparsh.Fragments.map.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.HashMap;

public class GoogleMapUi implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final OnMarkerClickListener onMarkerClickListener;

    private final Fragment fragment;
    private GoogleMap googleMap;
    private IconGenerator iconFactory;

    public GoogleMapUi(Fragment fragment, OnMarkerClickListener onMarkerClickListener) {
        this.fragment = fragment;
        this.onMarkerClickListener = onMarkerClickListener;
    }

    private void initGoogleMapMarkerIconFactory(Context context) {
        iconFactory = new IconGenerator(context);
        iconFactory.setBackground(context.getResources().getDrawable(R.drawable.marker1));
        iconFactory.setTextAppearance(R.style.myStyleText);
    }

    void onViewCreated(View v) {
        initGoogleMapMarkerIconFactory(v.getContext());
    }

    public void focusOnMap(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    public void putMarker(Location location, Integer count) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.alpha(1.0f);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon("" + (count == null ? 0 : count))));
        markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        markerOptions.position(location.getLocation());
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(location.getId());
        marker.setTitle("vvv");
    }

    public void putMarkers(HashMap<Location, Integer> locationPrasangCountHashmap) {
        for (Location location : locationPrasangCountHashmap.keySet()) {
            putMarker(location, locationPrasangCountHashmap.get(location));
        }
    }

    public void clearGoogleMap() {
        googleMap.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        this.googleMap.setMapStyle(new MapStyleOptions(fragment.getActivity().getResources().getString(R.string.style_json)));
        this.googleMap.setMaxZoomPreference(50);
        this.googleMap.setMinZoomPreference(4);

        LatLngBounds canadaBounds = new LatLngBounds(
                new LatLng(60.08251, -140.57825), // Canada SW bounds
                new LatLng(68.79696, -102.08215)  // Canada NE bounds
        );
        this.googleMap.setLatLngBoundsForCameraTarget(canadaBounds);
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(4));
        this.googleMap.setOnMarkerClickListener(this);

        UiSettings mapsetting = this.googleMap.getUiSettings();
        mapsetting.setZoomControlsEnabled(true);
        mapsetting.setZoomGesturesEnabled(true);
        mapsetting.setAllGesturesEnabled(true);
        mapsetting.setScrollGesturesEnabled(true);
    }

    @SuppressLint("MissingPermission")
    public void onLocationEnabled() {
        if (googleMap == null) return;
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return onMarkerClickListener.onMapMarkerClick(marker);
    }

    public interface OnMarkerClickListener {
        boolean onMapMarkerClick(@NonNull Marker marker);
    }
}
