package com.example.vicharan.Activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vicharan.R;
import com.example.vicharan.firebase.generic.DbInsertionListener;
import com.example.vicharan.firebase.location.DbLocation;
import com.example.vicharan.firebase.location.Location;
import com.example.vicharan.firebase.media.DbMedia;
import com.example.vicharan.firebase.media.Media;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.example.vicharan.firebase.prasang.Prasang;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;


public class Postadd extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    String cityName, address, googlePlaceId;
    LatLng latLng;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    AutocompleteSupportFragment autocompleteFragment, city;
    ImageView selectedImage1, selectedImage2, selectedImage3, upload;
    ImageView[] image;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    private TextInputLayout et_title, et_des, et_place, et_date, et_sutra, et_country;
    int photos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postadd);

        et_title = findViewById(R.id.title);
        et_des = findViewById(R.id.des);
        et_place = findViewById(R.id.place);
        et_date = findViewById(R.id.date);
        et_sutra = findViewById(R.id.sutra);
        et_country = findViewById(R.id.country);
        Button btn_calender = findViewById(R.id.calender);

        upload = findViewById(R.id.uploadImage);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};

        Button btn_postad = findViewById(R.id.post_ad);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.location_fragment);
        city = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.city);

        Calendar calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calender.clear();

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        calender.setTimeInMillis(today);

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        builder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = builder.build();
        builder.setSelection(today);


        btn_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                et_date.getEditText().setText(materialDatePicker.getHeaderText());

            }
        });
        setSearchUI();
        Places.initialize(getApplicationContext(), "AIzaSyDeFuuo_ueSXMlCCQQLUIFgFAs4Xo3ULNg");

        // Create a new PlacesClient instance
        PlacesClient placesClient = Places.createClient(this);
        // Initialize the AutocompleteSupportFragment.


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS));
        //autocompleteFragment.setTypeFilter(TypeFilter.CITIES);

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("", "Place: " + place.getAddressComponents());
                setSearchUI();
                latLng = place.getLatLng();
                address = place.getName();
                googlePlaceId = place.getId();
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });

        setCitySearchUI();
        city.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS));
        city.setTypeFilter(TypeFilter.CITIES);

        city.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("", "Place: " + place.getAddressComponents());
                setCitySearchUI();
                cityName = place.getName();
            }

            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        btn_postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fstore = FirebaseFirestore.getInstance();
                final String title = et_title.getEditText().getText().toString().trim();
                final String description = et_des.getEditText().getText().toString().trim();
                final String place = et_place.getEditText().getText().toString().trim();
                String sutra = et_sutra.getEditText().getText().toString().trim();
                String country = et_country.getEditText().getText().toString().trim();
                String date = et_date.getEditText().getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    return;
                } else if (title.length() > 65) {
                    Toast.makeText(Postadd.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (description.length() > 10000) {
                    Toast.makeText(Postadd.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (address == null) {
                    Toast.makeText(Postadd.this, "Please enter Address ", Toast.LENGTH_LONG).show();
                    return;
                } else if (cityName == null) {
                    Toast.makeText(Postadd.this, "Please select City", Toast.LENGTH_LONG).show();
                    return;
                } else if (country.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter Country name", Toast.LENGTH_LONG).show();
                    return;
                } else if (photos < 1) {
                    Toast.makeText(Postadd.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd = new ProgressDialog(Postadd.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();

                    Location location = new Location();
                    location.setLocation(new LatLng(latLng.latitude, latLng.longitude));
                    location.setUserId(userId);
                    location.setDescription(description);
                    location.setCountry(country);
                    location.setCity(cityName);
                    location.setPlace(place);
                    location.setGooglePlaceId(googlePlaceId);
                    location.setAddress(address);

                    Prasang prasang = new Prasang();
                    prasang.setUserId(userId);
                    //prasang.setTitle();
                    prasang.setSutra(sutra);
                    prasang.setDescription(description);
                    prasang.setDate(date);
                    //prasang.setNotes();
                    saveLocationDb(location, prasang);
                }
            }
        });
    }

    private void saveLocationDb(Location location, Prasang prasang) {
        DbLocation.insert(location, new DbInsertionListener() {
            @Override
            public void onSuccess(String locationId) {
                prasang.setLocationId(locationId);
                uploadImage(prasang);
                Toast.makeText(Postadd.this, " Post added Successfully ", Toast.LENGTH_SHORT).show();
                //pd.dismiss();
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
                String Error = e.getMessage();
                Toast.makeText(Postadd.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setCitySearchUI() {
        View fView = city.getView();
        EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
        etTextInput.setTextColor(Color.BLACK);
        etTextInput.setHintTextColor(Color.GRAY);
        etTextInput.setTextSize(14.5f);
        etTextInput.setHint("City");
        ImageButton close = fView.findViewById(R.id.places_autocomplete_clear_button);
        close.setVisibility(View.GONE);

    }

    private void setSearchUI() {
        View fView = autocompleteFragment.getView();
        EditText etTextInput = fView.findViewById(R.id.places_autocomplete_search_input);
        etTextInput.setTextColor(Color.BLACK);
        etTextInput.setHintTextColor(Color.GRAY);
        etTextInput.setTextSize(14.5f);
        etTextInput.setHint("Address");
        ImageButton close = fView.findViewById(R.id.places_autocomplete_clear_button);
        close.setVisibility(View.GONE);

    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(Postadd.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {
                    contenturi.clear();
                    upload.setImageResource(R.drawable.uploadnew);
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, ""), GALLERY_REQUEST_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                ClipData clipdata = data.getClipData();

                if (clipdata != null) {
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 4) {
                        Toast.makeText(this, "Please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }
                } else {
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }
    }

    private void uploadImage(Prasang prasang) {
        storageReference = FirebaseStorage.getInstance().getReference();

        long currentTimeMillis = System.currentTimeMillis();
        List<Media> medias = new LinkedList<>();
        for (int j = 0; j < contenturi.size(); j++) {

            String mediaName = (currentTimeMillis + j) + ".png";

            Media media = new Media();
            media.setName(mediaName);
            media.setMimeType(1);   // image
            medias.add(media);

            StorageReference ref = storageReference.child("images").child(prasang.getLocationId() + "/" + mediaName);
            ref.putFile(contenturi.get(j))
                    .addOnSuccessListener(taskSnapshot -> {

                    })
                    .addOnFailureListener(e -> Toast.makeText(Postadd.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
        saveImageInDb(prasang, medias);
    }

    private void saveImageInDb(Prasang prasang, List<Media> medias) {
        for (int i = 0; i < medias.size(); i++) {
            Media media = medias.get(i);
            final int n = i;
            DbMedia.insert(media, new DbInsertionListener() {
                @Override
                public void onSuccess(String id) {
                    prasang.addMedia(id);
                    if (n == medias.size() - 1) savePrasangDb(prasang);
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println(e);
                }
            });
        }
    }

    private void savePrasangDb(Prasang prasang) {
        DbPrasang.insert(prasang, new DbInsertionListener() {
            @Override
            public void onSuccess(String id) {
                System.out.println("prasang saved: " + id);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e);
            }
        });
    }
}