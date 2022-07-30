package com.example.vicharan.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.vicharan.Activity.ApartmentDialog;
import com.example.vicharan.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.ui.IconGenerator;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    FirebaseFirestore db;
    FusedLocationProviderClient mFusedLocationClient;
    boolean priceChanged = false;
    boolean bedChanged = false;
    ImageView filter;
    float min = 0, max = 5000;
    float minb = 0, maxb = 5;

    String city = "Montreal";

    LocationCallback mLocationCallback = new LocationCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onLocationResult(LocationResult locationResult) {
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                mLastLocation = location;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                putmarker(latLng);
            }
        }
    };

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter(getActivity());
            }
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        Places.initialize(getActivity().getApplicationContext(), "AIzaSyDeFuuo_ueSXMlCCQQLUIFgFAs4Xo3ULNg1");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(getActivity());
        // Initialize the AutocompleteSupportFragment.
        final AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.VIEWPORT, Place.Field.ADDRESS_COMPONENTS));
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                //  Log.i("", "Place: " + place.getAddressComponents());
                View fView = autocompleteFragment.getView();
                EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
                etTextInput.setTextColor(Color.BLACK);
                etTextInput.setHintTextColor(Color.GRAY);
                etTextInput.setTextSize(14.5f);

                ImageView ivClear = fView.findViewById(R.id.places_autocomplete_search_button);
                ImageButton close = fView.findViewById(R.id.places_autocomplete_clear_button);
                ivClear.setVisibility(View.GONE);
                close.setVisibility(View.GONE);

                putmarker(place.getLatLng());

                city = place.getName();
                getApartments(city);
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });
    }

    private void getApartments(String city) {
        mGoogleMap.clear();
        db = FirebaseFirestore.getInstance();
        db.collection("Apartment")
                .whereEqualTo("CityName", city).whereEqualTo("Status","Active")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LatLng latLng1 = new LatLng((Double) document.getData().get("Latitude"), (Double) document.getData().get("Longitude"));
                                float price = (Float.parseFloat((String) document.getData().get("Amount")));
                                float bed = (Float.parseFloat((String) document.getData().get("Bedroom")));
                                Log.d("", "DB BED" + bed);
                                if (priceChanged && bedChanged) {
                                    if (price >= min && price <= max && bed >= minb && bed <= maxb) {
                                        putApartmentMarker(latLng1, (String) document.getData().get("Amount"), (String) document.getData().get("Bedroom"), (String) document.getId());
                                    }
                                } else {
                                    putApartmentMarker(latLng1, (String) document.getData().get("Amount"), (String) document.getData().get("Bedroom"), (String) document.getId());
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void putApartmentMarker(LatLng latlng, String price, String bed, String ApartmentId) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.alpha(1.0f);
        IconGenerator iconFactory = new IconGenerator(getActivity());
        iconFactory.setBackground(getResources().getDrawable(R.drawable.marker1));
        iconFactory.setTextAppearance(R.style.myStyleText);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(price + "$$$$$$$$$$")));

        markerOptions.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        Marker m = mGoogleMap.addMarker(markerOptions);
        m.setTag(ApartmentId);
        m.setTitle("vvv");
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                ApartmentDialog alert = new ApartmentDialog();
                alert.showDialog(getActivity(), (String) marker.getTag());
                return true;
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mGoogleMap.setMaxZoomPreference(50);
        mGoogleMap.setMinZoomPreference(3);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mGoogleMap.setMyLocationEnabled(true);

                UiSettings mapsetting = googleMap.getUiSettings();
                mapsetting.setZoomControlsEnabled(true);
                mapsetting.setZoomGesturesEnabled(true);
                mapsetting.setAllGesturesEnabled(true);
                mapsetting.setScrollGesturesEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        } else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mGoogleMap.setMyLocationEnabled(true);
        }

    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    // Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void filter(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.activity_filter);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();

        wlp.gravity = Gravity.CENTER;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        final RangeSlider price_slider = dialog.findViewById(R.id.price_slider);
        final RangeSlider bedroom_slider = dialog.findViewById(R.id.bedroom_slider);
        final TextView to = dialog.findViewById(R.id.to);
        final TextView from = dialog.findViewById(R.id.from);
        final TextView tobed = dialog.findViewById(R.id.tobed);
        final TextView frombed = dialog.findViewById(R.id.frombed);

        price_slider.setValues(min, max);
        to.setText("Min " + min);
        from.setText("Max " + max);
        dialog.show();


        bedroom_slider.setValues(minb, maxb);
        tobed.setText("Min " + minb);
        frombed.setText("Max " + maxb);
        dialog.show();

        Button apply = dialog.findViewById(R.id.apply);
        Button reset = dialog.findViewById(R.id.reset);

        price_slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }
        });

        price_slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List price = slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
            }
        });

        bedroom_slider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                List bedroom = slider.getValues();
                minb = (float) bedroom.get(0);
                maxb = (float) bedroom.get(1);
                tobed.setText("Min " + minb);
                frombed.setText("Max " + maxb);
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                List bedroom = slider.getValues();
                minb = (float) bedroom.get(0);
                maxb = (float) bedroom.get(1);
                tobed.setText("Min " + minb);
                frombed.setText("Max " + maxb);
            }
        });

        bedroom_slider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List bedroom = slider.getValues();
                minb = (float) bedroom.get(0);
                maxb = (float) bedroom.get(1);
                tobed.setText("Min " + minb);
                frombed.setText("Max " + maxb);
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List price = price_slider.getValues();
                min = (float) price.get(0);
                max = (float) price.get(1);
                to.setText("Min " + min);
                from.setText("Max " + max);
                Log.d("", min + "");
                priceChanged = true;

                List bedroom = bedroom_slider.getValues();
                Log.d("", "VALUE SET " + bedroom);
                minb = (float) bedroom.get(0);
                Log.d("", "VALUE SET " + minb);
                maxb = (float) bedroom.get(1);
                Log.d("", "VALUE SET " + maxb);
                tobed.setText("Min " + minb);
                Log.d("", "VALUE SET " + tobed);
                frombed.setText("Max " + maxb);
                Log.d("", "VALUE SET " + frombed);
                bedChanged = true;
                dialog.dismiss();
                getApartments(city);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = 0;
                max = 5000;
                to.setText("Min " + min);
                from.setText("Max " + max);
                priceChanged = false;

                minb = 0;
                maxb = 5;
                tobed.setText("Min " + minb);
                frombed.setText("Max " + maxb);
                bedChanged = false;
                dialog.dismiss();
                getApartments(city);
            }
        });

    }

    private void putmarker(LatLng latLng) {
        //Place current location marker
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        getApartments(city);


    }


}

