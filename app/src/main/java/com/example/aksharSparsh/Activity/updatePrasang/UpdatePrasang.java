package com.example.aksharSparsh.Activity.updatePrasang;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aksharSparsh.Activity.savePrasang.SavePrasang;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.FirebaseUtils;
import com.example.aksharSparsh.firebase.generic.DbInsertionListener;
import com.example.aksharSparsh.firebase.location.DbLocation;
import com.example.aksharSparsh.firebase.location.GenericLocation;
import com.example.aksharSparsh.firebase.location.Location;
import com.example.aksharSparsh.firebase.media.DbMedia;
import com.example.aksharSparsh.firebase.media.Media;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;
import com.example.aksharSparsh.firebase.prasang.GenericPrasang;
import com.example.aksharSparsh.firebase.prasang.Prasang;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class UpdatePrasang extends AppCompatActivity {
    private static final String LOCATION_INTENT_KEY = "location";
    private static final String PRASANG_INTENT_KEY = "prasang";
    public static final int GALLERY_REQUEST_CODE = 105;

    private Location location;
    private Prasang prasang;

    String cityName, address, googlePlaceId, aptid;
    LatLng latLng;
    FirebaseFirestore fstore;
    AutocompleteSupportFragment autocompleteFragment, city;
    ImageView selectedImage1, selectedImage2, selectedImage3, upload, del, rent;
    ImageView[] image;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    int photos = 0, internetPhotos = 0;
    Boolean changed = false;
    private TextInputLayout et_title, et_des, et_place, et_date, et_sutra, et_country;

    public static void startActivity(AppCompatActivity caller, Location location, Prasang prasang) {
        Intent i = new Intent(caller, UpdatePrasang.class);
        i.putExtra("id", location.getId()); // TODO: delete later
        i.putExtra(LOCATION_INTENT_KEY, location);
        i.putExtra(PRASANG_INTENT_KEY, prasang);
        caller.startActivity(i);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        aptid = intent.getStringExtra("id");
        location = (Location) intent.getSerializableExtra(LOCATION_INTENT_KEY);
        prasang = (Prasang) intent.getSerializableExtra(PRASANG_INTENT_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prasang);
        parseIntent();

        et_title = findViewById(R.id.titleUpdate);
        et_des = findViewById(R.id.desUpdate);
        et_place = findViewById(R.id.placeUpdate);
        et_date = findViewById(R.id.dateUpdate);
        et_sutra = findViewById(R.id.sutraUpdate);
        et_country = findViewById(R.id.countryUpdate);
        Button btn_calender = findViewById(R.id.calenderUpdate);


        upload = findViewById(R.id.updateImage);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};
        del = findViewById(R.id.delete);
        rent = findViewById(R.id.rent);

        Button btn_postad = findViewById(R.id.post_ad);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.location_fragmentUpdate);
        city = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.cityUpdate);


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
                AddressComponents addressComponent = place.getAddressComponents();
                System.out.println(addressComponent);
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

        // Set up a PlaceSelectionListener to handle the response.
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

        fetchLocation();
        getImages();

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Delete", "Cancel"};
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdatePrasang.this);
                builder1.setTitle("Delete Post");
                builder1.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Delete")) {
                            delete();
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder1.show();

            }
        });
        upload.setOnClickListener(view -> selectImage());

        btn_postad.setOnClickListener(view -> {
            fstore = FirebaseFirestore.getInstance();

            final String title = et_title.getEditText().getText().toString().trim();
            final String description = et_des.getEditText().getText().toString().trim();
            final String place = et_place.getEditText().getText().toString().trim();
            String sutra = et_sutra.getEditText().getText().toString().trim();
            String country = et_country.getEditText().getText().toString().trim();
            String date = et_date.getEditText().getText().toString().trim();


            if (title.isEmpty()) {
                Toast.makeText(UpdatePrasang.this, "Please Enter Title", Toast.LENGTH_LONG).show();
            } else if (title.length() > 65) {
                Toast.makeText(UpdatePrasang.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
            } else if (description.length() > 10000) {
                Toast.makeText(UpdatePrasang.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
            } else if (place == null) {
                Toast.makeText(UpdatePrasang.this, "Please enter Place ", Toast.LENGTH_LONG).show();
                return;
            }else if (address == null) {
                Toast.makeText(UpdatePrasang.this, "Please enter Address ", Toast.LENGTH_LONG).show();
            } else if (cityName == null) {
                Toast.makeText(UpdatePrasang.this, "Please select City", Toast.LENGTH_LONG).show();
            } else if (country.isEmpty()) {
                Toast.makeText(UpdatePrasang.this, "Please enter Country name ", Toast.LENGTH_LONG).show();
            } else if (photos < 1) {
                Toast.makeText(UpdatePrasang.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
            } else {
                final ProgressDialog pd;
                pd = new ProgressDialog(UpdatePrasang.this);
                pd.setMessage("Loading...");
                //pd.show();

                location.setLocation(new LatLng(latLng.latitude, latLng.longitude));
                location.setGooglePlaceId(googlePlaceId);
                location.setAddress(address);
                GenericLocation englishVersionLocation = new GenericLocation();
                englishVersionLocation.setCountry(country);
                englishVersionLocation.setCity(cityName);
                englishVersionLocation.setPlace(place);
                location.setEnglishVersion(englishVersionLocation);

                prasang.setDate(date);
                GenericPrasang englishVersionPrasang = new GenericPrasang();
                englishVersionPrasang.setTitle(title);
                englishVersionPrasang.setSutra(sutra);
                englishVersionPrasang.setDescription(description);
                prasang.setEnglishVersion(englishVersionPrasang);

                upsertLocationAndUpdatePrasang(location, prasang);
            }
        });
    }

    private void upsertLocationAndUpdatePrasang(final Location location, final Prasang prasang) {
        DbLocation.getByGooglePlaceId(location.getGooglePlaceId(), loc -> {
            if (loc == null) {
                insertNewDbLocationAndSaveUpdatedPrasang(location, prasang);
            } else {
                prasang.setLocationId(loc.getId());
                updateDbPrasang(prasang);
            }
        });
    }

    private void insertNewDbLocationAndSaveUpdatedPrasang(Location location, final Prasang prasang) {
        DbLocation.insert(location, new DbInsertionListener() {
            @Override
            public void onSuccess(String id) {
                prasang.setLocationId(id);
                updateDbPrasang(prasang);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println("Error saving location: " + e.getMessage());
            }
        });
    }

    private void updateDbPrasang(Prasang prasang) {
        DbPrasang.update(prasang, unused -> {
            System.out.println("prasang updated successfully!");
            Toast.makeText(UpdatePrasang.this, "Prasang updated Successfully ", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updateImageInDb(Prasang prasang, List<Media> medias) {
        for (int i = 0; i < medias.size(); i++) {
            Media media = medias.get(i);
            final int n = i;
            DbMedia.update(media, unused -> {
            });
        }
    }

    private void getImages() {
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("images/" + aptid);

        listRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            storageReference = FirebaseStorage.getInstance().getReference();
                            String location = item.toString();
                            String image = location.substring(location.length() - 1);
                            System.out.println(image);
                            storageReference = FirebaseStorage.getInstance().getReference();
                            storageReference.child("images/" + aptid + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    contenturi.add(uri);
                                    internetPhotos = internetPhotos + 1;
                                    photos = 1;

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {

                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    private void delete() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Apartment").document(aptid);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        deleteImages();
                        Log.d("tagvv", "DocumentSnapshot successfully deleted!");
                        Toast.makeText(UpdatePrasang.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("tagvv", "Error deleting document", e);
                    }
                });

    }

    private void loadLocation(Location location) {
        googlePlaceId = location.getGooglePlaceId();
        latLng = location.getLocation();
        cityName = location.getEnglishVersion().getCity();
        address = location.getAddress();
        et_place.getEditText().setText(location.getEnglishVersion().getPlace());
        autocompleteFragment.setText(location.getAddress());
        city.setText(location.getEnglishVersion().getCity());
        et_country.getEditText().setText(location.getEnglishVersion().getCountry());
    }

    private void loadPrasang(Prasang prasang) {
        et_date.getEditText().setText(prasang.getDate());
        et_title.getEditText().setText(prasang.getEnglishVersion().getTitle());
        et_des.getEditText().setText(prasang.getEnglishVersion().getDescription());
        et_sutra.getEditText().setText(prasang.getEnglishVersion().getSutra());
    }

    private void fetchPrasang(Location location) {
        DbPrasang.getByLocationId(location.getId(), (List<Prasang> prasangs) -> {
            if (prasangs == null || prasangs.isEmpty()) {
                System.out.println("no prasangs found");
                return;
            }
            Prasang prasang = prasangs.get(0);
            loadPrasang(prasang);
        });
    }

    private void fetchLocation() {
        DbLocation.getById(aptid, (Location location) -> {
            loadLocation(location);
            fetchPrasang(location);
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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdatePrasang.this);
        builder1.setTitle("Add Photo!");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose from Gallery")) {

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
                contenturi.clear();
                deleteImages();
                //  Toast.makeText(this, "" + internetPhotos, Toast.LENGTH_SHORT).show();

                if (clipdata != null) {
                    changed = true;
                    photos = clipdata.getItemCount();
                    if (clipdata.getItemCount() > 4) {
                        Toast.makeText(this, "please select only four items", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    for (int i = 0; i < clipdata.getItemCount(); i++) {
                        ClipData.Item item = clipdata.getItemAt(i);
                        contenturi.add(item.getUri());
                    }

                } else {
                    changed = true;
                    contenturi.add(data.getData());
                    photos = 1;
                }
            }
        }

    }

    private void deleteImages() {

        storageReference = FirebaseStorage.getInstance().getReference();

// Create a reference to the file to delete
        for (int i = 0; i < internetPhotos; i++) {
            StorageReference desertRef = storageReference.child("images/" + aptid + "/" + i);
// Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                }
            });
        }
    }

    private void uploadImage(Prasang prasang) {
        long currentTimeMillis = System.currentTimeMillis();
        List<Media> medias = new LinkedList<>();
        for (int j = 0; j < contenturi.size(); j++) {

            String mediaName = (currentTimeMillis + j) + ".png";

            Media media = new Media();
            media.setName(mediaName);
            media.setMimeType(1);   // image
            medias.add(media);

            FirebaseUtils.saveImage(prasang.getId(), mediaName, contenturi.get(j), null, e -> {
                Toast.makeText(UpdatePrasang.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        updateImageInDb(prasang, medias);
    }
}