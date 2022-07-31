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
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;


public class UpdateAd extends AppCompatActivity {

    public static final int GALLERY_REQUEST_CODE = 105;
    String cityName, address, aptid;
    LatLng latLng;
    FirebaseFirestore fstore;
    FirebaseAuth auth;
    AutocompleteSupportFragment autocompleteFragment, city;
    ImageView selectedImage1;
    ImageView selectedImage2;
    ImageView selectedImage3;
    ImageView upload;
    ImageView del;
    ImageView rent;
    ImageView[] image;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    int photos = 0, internetPhotos = 0;
    Boolean changed = false;
    private TextInputLayout et_title, et_des, et_place, et_date, et_sutra, et_country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ad);
        Intent intent = getIntent();
        aptid = intent.getStringExtra("id");

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

        getdata();
        getImages();

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] options = {"Delete", "Cancel"};
                AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateAd.this);
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
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fstore = FirebaseFirestore.getInstance();
                DocumentReference docRef = fstore.collection("Apartment").document(aptid);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Map<String, Object> data1 = document.getData();
                                String Status = data1.get("Status").toString();
                                if (Status.equals("Active")) {
                                    sold();
                                } else {
                                    rentagain();
                                }

                                Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d("tagvv", "No such document");
                            }
                        } else {
                            Log.d("tagvv", "get failed with ", task.getException());
                        }
                    }
                });

            }
        });


        btn_postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fstore = FirebaseFirestore.getInstance();

                final String Title = et_title.getEditText().getText().toString().trim();
                final String Description = et_des.getEditText().getText().toString().trim();
                final String Place = et_place.getEditText().getText().toString().trim();
                String Sutra = et_sutra.getEditText().getText().toString().trim();
                String Country = et_country.getEditText().getText().toString().trim();
                String Date = et_date.getEditText().getText().toString().trim();


                if (Title.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    return;
                } else if (Title.length() > 65) {
                    Toast.makeText(UpdateAd.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.length() > 10000) {
                    Toast.makeText(UpdateAd.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (address == null) {
                    Toast.makeText(UpdateAd.this, "Please enter Address ", Toast.LENGTH_LONG).show();
                    return;
                } else if (cityName == null) {
                    Toast.makeText(UpdateAd.this, "Please select City", Toast.LENGTH_LONG).show();
                    return;

                } else if (Country.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please enter Size ", Toast.LENGTH_LONG).show();
                    return;
                } else if (photos < 1) {
                    Toast.makeText(UpdateAd.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd;
                    pd = new ProgressDialog(UpdateAd.this);
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
                    userMap.put("Place", Place);
                    userMap.put("Date", Date);
                    userMap.put("Sutra", Sutra);
                    userMap.put("Country", Country);
                    userMap.put("CityName", cityName);
                    userMap.put("Latitude", latLng.latitude);
                    userMap.put("Longitude", latLng.longitude);
                    userMap.put("Address", address);
                    userMap.put("Status", "Active");


                    fstore.collection("Apartment").document(aptid)
                            .set(userMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    if (changed) {
                                        uploadImage((aptid));
                                    }
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                    SystemClock.sleep(3000);
                                    Toast.makeText(UpdateAd.this, "Post Updated Successfully", Toast.LENGTH_SHORT).show();
                                    pd.dismiss();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });

                }

            }
        });


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
                Toast.makeText(UpdateAd.this, "Post Deleted Successfully", Toast.LENGTH_SHORT).show();
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

    private void getdata() {
        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Apartment").document(aptid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();

                        String Title1 = data1.get("Title").toString();
                        String Description1 = data1.get("Description").toString();
                        String Place1 = data1.get("Place").toString();
                        String Address1 = data1.get("Address").toString();
                        String CityName1 = data1.get("CityName").toString();
                        String Date1 = data1.get("Date").toString();
                        String Sutra1 = data1.get("Sutra").toString();
                        String Status = data1.get("Status").toString();
                        double latitude = (Double) data1.get("Latitude");
                        double longitude = (Double) data1.get("Longitude");
                        latLng = new LatLng(latitude, longitude);
                        cityName = CityName1;
                        address = Address1;


                        et_title.getEditText().setText(Title1);
                        et_des.getEditText().setText(Description1);
                        et_place.getEditText().setText(Place1);
                        autocompleteFragment.setText(Address1);
                        city.setText(CityName1);
                        et_date.getEditText().setText(Date1);
                        et_sutra.getEditText().setText(Sutra1);


                        if (Status.equals("Active")) {
                            rent.setImageResource(R.drawable.rented);

                            new SimpleTooltip.Builder(UpdateAd.this)
                                    .anchorView(rent)
                                    .text("Click to mark it as Rented")
                                    .gravity(Gravity.BOTTOM)
                                    .dismissOnOutsideTouch(true)
                                    .dismissOnInsideTouch(false)
                                    .build()
                                    .show();
                        } else {
                            rent.setImageResource(R.drawable.rent);

                            new SimpleTooltip.Builder(UpdateAd.this)
                                    .anchorView(rent)
                                    .text("Click to Rent it Again")
                                    .gravity(Gravity.BOTTOM)
                                    .dismissOnOutsideTouch(true)
                                    .dismissOnInsideTouch(false)
                                    .build()
                                    .show();
                        }

                        Log.d("tagvv", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("tagvv", "No such document");
                    }
                } else {
                    Log.d("tagvv", "get failed with ", task.getException());
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

    private void sold() {
        final CharSequence[] options = {"Yes", "No"};
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(UpdateAd.this);
        builder1.setTitle("Is the house Rented ?");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yes")) {
                    fstore = FirebaseFirestore.getInstance();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Status", "Inactive");
                    fstore.collection("Apartment").document(aptid)
                            .update(userMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                    Toast.makeText(UpdateAd.this, "Rented", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });

                } else if (options[item].equals("NO")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();

    }

    private void rentagain() {
        final CharSequence[] options = {"Yes", "No"};
        androidx.appcompat.app.AlertDialog.Builder builder1 = new androidx.appcompat.app.AlertDialog.Builder(UpdateAd.this);
        builder1.setTitle("Do you want to Rent it again?");
        builder1.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Yes")) {
                    fstore = FirebaseFirestore.getInstance();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("Status", "Active");
                    fstore.collection("Apartment").document(aptid)
                            .update(userMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "DocumentSnapshot successfully written!");
                                    Toast.makeText(UpdateAd.this, "Rented Again Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("TAG", "Error writing document", e);
                                }
                            });

                } else if (options[item].equals("NO")) {
                    dialog.dismiss();
                }
            }
        });
        builder1.show();

    }

    private void selectImage() {
        final CharSequence[] options = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder1 = new AlertDialog.Builder(UpdateAd.this);
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

                            Toast.makeText(UpdateAd.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}