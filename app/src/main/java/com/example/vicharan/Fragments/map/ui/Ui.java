package com.example.vicharan.Fragments.map.ui;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.vicharan.Fragments.map.MapFragment;
import com.example.vicharan.R;
import com.example.vicharan.dialog.PrasangDialog;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class Ui implements PlaceSelectionListener, GoogleMapUi.OnMarkerClickListener {
    private final MapFragment fragment;
    private final UiListener uiListener;
    private final GoogleMapUi googleMapUi;

    private AutocompleteSupportFragment autocompleteFragment;

    public Ui(MapFragment fragment, UiListener uiListener) {
        this.fragment = fragment;
        this.uiListener = uiListener;
        googleMapUi = new GoogleMapUi(fragment, this);
    }

    public GoogleMapUi getGoogleMapUi() {
        return googleMapUi;
    }

    public void onViewCreated(View v) {
        getGoogleMapUi().onViewCreated(v);
        SupportMapFragment mapFrag = (SupportMapFragment) fragment.getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFrag == null) {
            throw new RuntimeException("fragment not initialized");
        }
        mapFrag.getMapAsync(getGoogleMapUi());
        initGooglePlaceAutoComplete();
    }

    private void initGooglePlaceAutoComplete() {
        autocompleteFragment = (AutocompleteSupportFragment) fragment.getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT, Place.Field.ADDRESS_COMPONENTS));
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);
        autocompleteFragment.setOnPlaceSelectedListener(this);
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

        getGoogleMapUi().focusOnMap(place.getLatLng());
        uiListener.onPlaceSelectedWithGoogleAutoComplete(place);
    }

    @Override
    public void onError(@NonNull Status status) {
    }

    @Override
    public boolean onMapMarkerClick(@NonNull Marker marker) {
        String locationId = (String) marker.getTag();
        PrasangDialog prasangDialog = new PrasangDialog(fragment.getActivity());
        prasangDialog.loadDataAndShowDialog(locationId);
        return true;
    }

    public interface UiListener {
        void onPlaceSelectedWithGoogleAutoComplete(@NonNull Place place);
    }
}