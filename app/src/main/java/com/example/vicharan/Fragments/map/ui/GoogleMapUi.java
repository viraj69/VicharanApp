package com.example.vicharan.Fragments.map.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.vicharan.R;
import com.example.vicharan.firebase.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

public class GoogleMapUi implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private final  OnMarkerClickListener onMarkerClickListener;

    private GoogleMap googleMap;
    private IconGenerator iconFactory;

    public GoogleMapUi(OnMarkerClickListener onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;
    }

    private void initGoogleMapMarkerIconFactory(Context context) {
        iconFactory = new IconGenerator(context);
        iconFactory.setBackground(context.getResources().getDrawable(R.drawable.locationpin));
        iconFactory.setTextAppearance(R.style.myStyleText);
    }

    void onViewCreated(View v) {
        initGoogleMapMarkerIconFactory(v.getContext());
    }

    public void focusOnMap(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    public void putMarker(Location location) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.alpha(1.0f);
        //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(place)));
        markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        markerOptions.position(location.getLocation());
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(location.getId());
        marker.setTitle("vvv");
    }

    public void putMarkers(List<Location> locations) {
        for (Location location : locations) putMarker(location);
    }

    public void clearGoogleMap() {
        googleMap.clear();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        this.googleMap.setMaxZoomPreference(50);
        this.googleMap.setMinZoomPreference(3);

        double bottomCordinate = 41.232345;
        double leftCordinate = -129.216371;
        double topCordinate = 62.341938;
        double rightCordinate = -55.564033;

        LatLngBounds canadaBounds = new LatLngBounds(
                new LatLng(bottomCordinate, leftCordinate), // SW bounds
                new LatLng(topCordinate, rightCordinate)  // NE bounds
        );

        googleMap.setLatLngBoundsForCameraTarget(canadaBounds);
        googleMap.setOnMarkerClickListener(this);

        UiSettings mapsetting = googleMap.getUiSettings();
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
