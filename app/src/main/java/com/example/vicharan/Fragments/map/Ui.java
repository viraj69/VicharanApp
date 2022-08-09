package com.example.vicharan.Fragments.map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.vicharan.Activity.ApartmentDialog;
import com.example.vicharan.R;
import com.example.vicharan.firebase.Apartment.Apartment;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.ui.IconGenerator;
import java.util.Arrays;
import java.util.List;

class Ui implements OnMapReadyCallback, PlaceSelectionListener, GoogleMap.OnMarkerClickListener {
    private final MapFragment fragment;
    private final UiListener uiListener;

    private GoogleMap googleMap;
    private AutocompleteSupportFragment autocompleteFragment;
    private IconGenerator iconFactory;

    Ui(MapFragment fragment, UiListener uiListener) {
        this.fragment = fragment;
        this.uiListener = uiListener;
    }

    private void initGoogleMapMarkerIconFactory() {
        iconFactory = new IconGenerator(fragment.getActivity());
        iconFactory.setBackground(fragment.getActivity().getResources().getDrawable(R.drawable.marker1));
        iconFactory.setTextAppearance(R.style.myStyleText);
    }

    void onViewCreated(View v) {
        initGoogleMapMarkerIconFactory();
        SupportMapFragment mapFrag = (SupportMapFragment) fragment.getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag == null) {
            throw new RuntimeException("fragment not initialized");
        }
        mapFrag.getMapAsync(this);
        initGooglePlaceAutoComplete();
    }

    private void initGooglePlaceAutoComplete() {
        autocompleteFragment = (AutocompleteSupportFragment) fragment.getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT, Place.Field.ADDRESS_COMPONENTS));
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }

    void focusOnMap(LatLng latLng) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
    }

    void putApartmentMarker(Apartment apartment) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.alpha(1.0f);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon()));
        markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());
        markerOptions.position(apartment.getLocation());
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(apartment.getId());
        marker.setTitle("vvv");
    }

    void putApartmentMarkers(List<Apartment> apartments) {
        for (Apartment apartment : apartments) putApartmentMarker(apartment);
    }

    void clearGoogleMap() {
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
    void onLocationEnabled() {
        if (googleMap == null) return;
        googleMap.setMyLocationEnabled(true);
    }

    @Override
    public void onPlaceSelected(@NonNull Place place) {
        View fView = autocompleteFragment.getView();
        EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
        etTextInput.setTextColor(Color.BLACK);
        etTextInput.setHintTextColor(Color.GRAY);
        etTextInput.setTextSize(14.5f);

        ImageView ivClear = fView.findViewById(R.id.places_autocomplete_search_button);
        ImageButton close = fView.findViewById(R.id.places_autocomplete_clear_button);
        ivClear.setVisibility(View.GONE);
        close.setVisibility(View.GONE);

        focusOnMap(place.getLatLng());
        uiListener.onPlaceSelectedWithGoogleAutoComplete(place);
    }

    @Override
    public void onError(@NonNull Status status) {
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        ApartmentDialog alert = new ApartmentDialog();
        alert.showDialog(fragment.getActivity(), (String) marker.getTag());
        return true;
    }

    interface UiListener {
        void onPlaceSelectedWithGoogleAutoComplete(@NonNull Place place);
    }
}