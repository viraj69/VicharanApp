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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vicharan.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


public class Postadd extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    String cityName, address;
    LatLng latLng;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    AutocompleteSupportFragment autocompleteFragment, city;
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload;
    ImageView[] image;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    private TextInputLayout et_title, et_des, et_amt, et_date, et_size;
    int photos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postadd);

        et_title = findViewById(R.id.title);
        et_des = findViewById(R.id.des);
        et_amt = findViewById(R.id.amount);
        et_date = findViewById(R.id.date);
        et_size = findViewById(R.id.size);
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
        Places.initialize(getApplicationContext(), "AIzaSyDzkhBJZpa16X7NMsbveeggrcSGfT-IsH0");

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
                final String Title = et_title.getEditText().getText().toString().trim();
                final String Description = et_des.getEditText().getText().toString().trim();
                final String Amount = et_amt.getEditText().getText().toString().trim();
                String Size = et_size.getEditText().getText().toString().trim();
                String MoveInDate = et_date.getEditText().getText().toString().trim();



                if (Title.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    return;
                } else if (Title.length() > 65) {
                    Toast.makeText(Postadd.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.length() > 10000) {
                    Toast.makeText(Postadd.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter Amount ", Toast.LENGTH_LONG).show();
                    return;
                }  else if (address == null) {
                    Toast.makeText(Postadd.this, "Please enter Address ", Toast.LENGTH_LONG).show();
                    return;
                } else if (cityName == null) {
                    Toast.makeText(Postadd.this, "Please select City", Toast.LENGTH_LONG).show();
                    return;
                }  else if (MoveInDate.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter MoveInDate ", Toast.LENGTH_LONG).show();
                    return;
                }  else if (Size.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter Size ", Toast.LENGTH_LONG).show();
                    return;
                }  else if (photos < 1) {
                    Toast.makeText(Postadd.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd;
                    pd = new ProgressDialog(Postadd.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    Log.v("tagvv", " " + uid);

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("UserID", uid);
                    userMap.put("Title", Title);
                    userMap.put("Description", Description);
                    userMap.put("Amount", Amount);
                    userMap.put("MoveInDate", MoveInDate);
                    userMap.put("Size", Size);
                    userMap.put("CityName", cityName);
                    userMap.put("Latitude", latLng.latitude);
                    userMap.put("Longitude", latLng.longitude);
                    userMap.put("Address", address);
                    userMap.put("Status", "Active");


                    fstore.collection("Apartment").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            uploadImage((String) documentReference.getId());
                            Toast.makeText(Postadd.this, " Post added Successfully ", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String Error = e.getMessage();
                            Toast.makeText(Postadd.this, " Error:" + Error, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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

    private void uploadImage(String id) {
        storageReference = FirebaseStorage.getInstance().getReference();
        for (int j = 0; j < contenturi.size(); j++) {

            StorageReference ref = storageReference.child("images").child(id + "/" + j);
            ref.putFile(contenturi.get(j))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(Postadd.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }

    }


}