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
    ImageView selectedImage, selectedImage1, selectedImage2, selectedImage3, upload, del, rent;
    ImageView[] image;
    FirebaseStorage storage;
    StorageReference storageReference;
    ArrayList<Uri> contenturi = new ArrayList<Uri>();
    int photos = 0, internetPhotos = 0;
    Boolean changed = false;
    private TextInputLayout et_title, et_des, et_amt, et_unit, et_pnum, et_date, et_bath, et_bed, et_pet, et_size, et_smoke, et_parking;
    private RadioGroup rbfurnished, rbflaundry, rbLaundryb, rbdishwasher, rbfridge, rbair_conditioning, rbyard, rbbalcony, rbramp, rbaids, rbsuite, rbhydro, rbheat, rbwater, rbtv, rbinternet, rbgym, rbpool, rbconcierge, rbstorage, rbsecurity, rbelevator, rbwheelchair, rblabels, rbaudio, rbbicycle;
    private RadioButton btn_flaundry, btn_furnished, btn_Laundryb, btn_dishwasher, btn_fridge, btn_air_conditioning, btn_yard, btn_balcony, btn_ramp, btn_aids, btn_suite, btn_hydro, btn_heat, btn_water, btn_tv, btn_internet, btn_gym, btn_pool, btn_concierge, btn_storage, btn_security, btn_elevator, btn_wheelchair, btn_labels, btn_audio, btn_bicycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ad);
        Intent intent = getIntent();
        aptid = intent.getStringExtra("id");


        et_title = findViewById(R.id.titleUpdate);
        et_des = findViewById(R.id.desUpdate);
        et_amt = findViewById(R.id.amountUpdate);
        et_unit = findViewById(R.id.unit1Update);
        et_pnum = findViewById(R.id.pnumUpdate);
        et_date = findViewById(R.id.dateUpdate);
        et_bath = findViewById(R.id.bathroom1Update);
        et_bed = findViewById(R.id.bedroom1Update);
        et_pet = findViewById(R.id.pet1Update);
        et_size = findViewById(R.id.sizeUpdate);
        et_smoke = findViewById(R.id.smoke1Update);
        et_parking = findViewById(R.id.parking1Update);
        Button btn_calender = findViewById(R.id.calenderUpdate);


        upload = findViewById(R.id.updateImage);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};
        del = findViewById(R.id.delete);
        rent = findViewById(R.id.rent);


        Button btn_postad = findViewById(R.id.post_ad);


        rbfurnished = findViewById(R.id.rbfurnishedUpdate);
        rbflaundry = findViewById(R.id.rbflaundryUpdate);
        rbLaundryb = findViewById(R.id.rbLaundrybUpdate);
        rbdishwasher = findViewById(R.id.rbdishwasherUpdate);
        rbfridge = findViewById(R.id.rbfridgeUpdate);
        rbair_conditioning = findViewById(R.id.rbair_conditioningUpdate);
        rbyard = findViewById(R.id.rbyardUpdate);
        rbbalcony = findViewById(R.id.rbbalconyUpdate);
        rbramp = findViewById(R.id.rbrampUpdate);
        rbaids = findViewById(R.id.rbaidsUpdate);
        rbsuite = findViewById(R.id.rbsuiteUpdate);
        rbhydro = findViewById(R.id.rbhydroUpdate);
        rbheat = findViewById(R.id.rbheatUpdate);
        rbwater = findViewById(R.id.rbwaterUpdate);
        rbtv = findViewById(R.id.rbtvUpdate);
        rbinternet = findViewById(R.id.rbinternetUpdate);
        rbgym = findViewById(R.id.rbgymUpdate);
        rbpool = findViewById(R.id.rbpoolUpdate);
        rbconcierge = findViewById(R.id.rbconciergeUpdate);
        rbstorage = findViewById(R.id.rbstorageUpdate);
        rbsecurity = findViewById(R.id.rbsecurityUpdate);
        rbelevator = findViewById(R.id.rbelevatorUpdate);
        rbwheelchair = findViewById(R.id.rbwheelchairUpdate);
        rblabels = findViewById(R.id.rblabelsUpdate);
        rbaudio = findViewById(R.id.rbaudioUpdate);
        rbbicycle = findViewById(R.id.rbbicycleUpdate);


        AutoCompleteTextView unit = findViewById(R.id.unitUpdate);
        AutoCompleteTextView bedroom = findViewById(R.id.bedroomUpdate);
        AutoCompleteTextView bathroom = findViewById(R.id.bathroomUpdate);
        AutoCompleteTextView pet = findViewById(R.id.petUpdate);
        AutoCompleteTextView smoke = findViewById(R.id.smokeUpdate);
        AutoCompleteTextView parking = findViewById(R.id.parkingUpdate);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.location_fragmentUpdate);
        city = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.cityUpdate);


        String[] units = new String[]{"Apartment", "Room", "House", "Condo"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                units
        );

        unit.setAdapter(adapter1);

        String[] Bedrooms = new String[]{"1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                Bedrooms
        );

        bedroom.setAdapter(adapter2);


        String[] bathrooms = new String[]{"1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                bathrooms
        );

        bathroom.setAdapter(adapter3);

        String[] pets = new String[]{"Yes", "No", "Limited"};

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                pets
        );

        pet.setAdapter(adapter4);

        String[] smokes = new String[]{"Yes", "No", "Outdoors only"};

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                smokes
        );

        smoke.setAdapter(adapter5);

        String[] parkings = new String[]{"0", "1", "2", "3+"};

        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(
                UpdateAd.this,
                R.layout.dropdown_item,
                parkings
        );

        parking.setAdapter(adapter6);


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
                final String Amount = et_amt.getEditText().getText().toString().trim();
                String Unit = et_unit.getEditText().getText().toString().trim();
                String PhoneNumber = et_pnum.getEditText().getText().toString().trim();
                String Bathroom = et_bath.getEditText().getText().toString().trim();
                String Bedroom = et_bed.getEditText().getText().toString().trim();
                String PetFriendly = et_pet.getEditText().getText().toString().trim();
                String Size = et_size.getEditText().getText().toString().trim();
                String MoveInDate = et_date.getEditText().getText().toString().trim();
                String SmokePermitted = et_smoke.getEditText().getText().toString().trim();
                String ParkingIncluded = et_parking.getEditText().getText().toString().trim();

                if (Title.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please Enter Title", Toast.LENGTH_LONG).show();
                    return;
                } else if (Title.length() > 65) {
                    Toast.makeText(UpdateAd.this, "Title should be 64 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please Enter Description", Toast.LENGTH_LONG).show();
                    return;
                } else if (Description.length() > 10000) {
                    Toast.makeText(UpdateAd.this, "Title should be 100000 letters in length", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please enter Amount ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Amount.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(UpdateAd.this, "Please Enter Amount in Digit", Toast.LENGTH_LONG).show();
                    return;
                }  else if (address == null) {
                    Toast.makeText(UpdateAd.this, "Please enter Address ", Toast.LENGTH_LONG).show();
                    return;
                } else if (cityName == null) {
                    Toast.makeText(UpdateAd.this, "Please select City", Toast.LENGTH_LONG).show();
                    return;

                } else if (Unit.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please select Unit", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Unit.equals("Apartment") || Unit.equals("Room") || Unit.equals("House") || Unit.equals("Condo"))) {
                    Toast.makeText(UpdateAd.this, "Please Select Unit from DropDown", Toast.LENGTH_LONG).show();
                    et_unit.getEditText().getText().clear();
                    return;
                } else if (PhoneNumber.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please enter PhoneNumber ", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(UpdateAd.this, "Please Enter PhoneNumber in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.length() < 10 || PhoneNumber.length() > 12) {
                    Toast.makeText(UpdateAd.this, "Please enter 10 to 12 digit PhoneNumber", Toast.LENGTH_LONG).show();
                    return;
                } else if (MoveInDate.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please enter MoveInDate ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Bathroom.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please select Bathroom", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Bathroom.equals("1") || Bathroom.equals("1.5") || Bathroom.equals("2") || Bathroom.equals("2.5") || Bathroom.equals("3") || Bathroom.equals("3.5") || Bathroom.equals("4") || Bathroom.equals("4.5") || Bathroom.equals("5") || Bathroom.equals("5.5") || Bathroom.equals("6"))) {
                    Toast.makeText(UpdateAd.this, "Please Select Bathroom from DropDown", Toast.LENGTH_LONG).show();
                    et_bath.getEditText().getText().clear();
                    return;
                } else if (Bedroom.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please select Bedroom", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Bedroom.equals("1") || Bedroom.equals("2") || Bedroom.equals("3") || Bedroom.equals("4") || Bedroom.equals("5"))) {
                    Toast.makeText(UpdateAd.this, "Please Select Bedroom from DropDown", Toast.LENGTH_LONG).show();
                    et_bed.getEditText().getText().clear();
                    return;
                } else if (PetFriendly.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please select Pet Friendly", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(PetFriendly.equals("Yes") || PetFriendly.equals("No") || PetFriendly.equals("Limited"))) {
                    Toast.makeText(UpdateAd.this, "Please Select Pet Friendly from DropDown", Toast.LENGTH_LONG).show();
                    et_pet.getEditText().getText().clear();
                    return;
                } else if (Size.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please enter Size ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Size.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(UpdateAd.this, "Please Enter Size in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (SmokePermitted.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please select Smoke Permitted", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(SmokePermitted.equals("Yes") || SmokePermitted.equals("No") || SmokePermitted.equals("Outdoors only"))) {
                    Toast.makeText(UpdateAd.this, "Please Select Smoke Permitted from DropDown", Toast.LENGTH_LONG).show();
                    et_smoke.getEditText().getText().clear();
                    return;
                } else if (ParkingIncluded.isEmpty()) {
                    Toast.makeText(UpdateAd.this, "Please select Parking Included", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(ParkingIncluded.equals("0") || ParkingIncluded.equals("1") || ParkingIncluded.equals("2") || ParkingIncluded.equals("3+"))) {
                    Toast.makeText(UpdateAd.this, "Please Select Parking Included from DropDown", Toast.LENGTH_LONG).show();
                    et_parking.getEditText().getText().clear();
                    return;

                } else if (photos < 1) {
                    Toast.makeText(UpdateAd.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd;
                    pd = new ProgressDialog(UpdateAd.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    // TODO: remove below code from L487 to L620
                    int selectedId1 = rbfurnished.getCheckedRadioButtonId();
                    btn_furnished = findViewById(selectedId1);
                    String Furnished = btn_furnished.getText().toString().trim();
                    Log.v("tagvv", " " + Furnished);

                    int selectedId2 = rbflaundry.getCheckedRadioButtonId();
                    btn_flaundry = findViewById(selectedId2);
                    String UnitLaundry = btn_flaundry.getText().toString().trim();
                    Log.v("tagvv", " " + UnitLaundry);

                    int selectedId3 = rbLaundryb.getCheckedRadioButtonId();
                    btn_Laundryb = findViewById(selectedId3);
                    String BuildingLaundry = btn_Laundryb.getText().toString().trim();
                    Log.v("tagvv", " " + BuildingLaundry);

                    int selectedId4 = rbdishwasher.getCheckedRadioButtonId();
                    btn_dishwasher = findViewById(selectedId4);
                    String Dishwasher = btn_dishwasher.getText().toString().trim();
                    Log.v("tagvv", " " + Dishwasher);

                    int selectedId5 = rbfridge.getCheckedRadioButtonId();
                    btn_fridge = findViewById(selectedId5);
                    String Fridge = btn_fridge.getText().toString().trim();
                    Log.v("tagvv", " " + Fridge);

                    int selectedId6 = rbair_conditioning.getCheckedRadioButtonId();
                    btn_air_conditioning = findViewById(selectedId6);
                    String AirConditioning = btn_air_conditioning.getText().toString().trim();
                    Log.v("tagvv", " " + AirConditioning);

                    int selectedId7 = rbyard.getCheckedRadioButtonId();
                    btn_yard = findViewById(selectedId7);
                    String Yard = btn_yard.getText().toString().trim();
                    Log.v("tagvv", " " + Yard);

                    int selectedId8 = rbbalcony.getCheckedRadioButtonId();
                    btn_balcony = findViewById(selectedId8);
                    String Balcony = btn_balcony.getText().toString().trim();
                    Log.v("tagvv", " " + Balcony);

                    int selectedId9 = rbramp.getCheckedRadioButtonId();
                    btn_ramp = findViewById(selectedId9);
                    String Barrier_free_Entrance_Ramps = btn_ramp.getText().toString().trim();
                    Log.v("tagvv", " " + Barrier_free_Entrance_Ramps);

                    int selectedId10 = rbaids.getCheckedRadioButtonId();
                    btn_aids = findViewById(selectedId10);
                    String VisualAids = btn_aids.getText().toString().trim();
                    Log.v("tagvv", " " + VisualAids);

                    int selectedId11 = rbsuite.getCheckedRadioButtonId();
                    btn_suite = findViewById(selectedId11);
                    String Accessible_Washrooms_in_suite = btn_suite.getText().toString().trim();
                    Log.v("tagvv", " " + Accessible_Washrooms_in_suite);

                    int selectedId12 = rbhydro.getCheckedRadioButtonId();
                    btn_hydro = findViewById(selectedId12);
                    String Hydro = btn_hydro.getText().toString().trim();
                    Log.v("tagvv", " " + Hydro);

                    int selectedId13 = rbheat.getCheckedRadioButtonId();
                    btn_heat = findViewById(selectedId13);
                    String Heat = btn_heat.getText().toString().trim();
                    Log.v("tagvv", " " + Heat);

                    int selectedId14 = rbwater.getCheckedRadioButtonId();
                    btn_water = findViewById(selectedId14);
                    String Water = btn_water.getText().toString().trim();
                    Log.v("tagvv", " " + Water);

                    int selectedId15 = rbtv.getCheckedRadioButtonId();
                    btn_tv = findViewById(selectedId15);
                    String Tv = btn_tv.getText().toString().trim();
                    Log.v("tagvv", " " + Tv);

                    int selectedId16 = rbinternet.getCheckedRadioButtonId();
                    btn_internet = findViewById(selectedId16);
                    String Internet = btn_internet.getText().toString().trim();
                    Log.v("tagvv", " " + Internet);

                    int selectedId17 = rbgym.getCheckedRadioButtonId();
                    btn_gym = findViewById(selectedId17);
                    String Gym = btn_gym.getText().toString().trim();
                    Log.v("tagvv", " " + Gym);

                    int selectedId18 = rbpool.getCheckedRadioButtonId();
                    btn_pool = findViewById(selectedId18);
                    String Pool = btn_pool.getText().toString().trim();
                    Log.v("tagvv", " " + Pool);

                    int selectedId19 = rbconcierge.getCheckedRadioButtonId();
                    btn_concierge = findViewById(selectedId19);
                    String Concierge = btn_concierge.getText().toString().trim();
                    Log.v("tagvv", " " + Concierge);

                    int selectedId20 = rbstorage.getCheckedRadioButtonId();
                    btn_storage = findViewById(selectedId20);
                    String Storage_Space = btn_storage.getText().toString().trim();
                    Log.v("tagvv", " " + Storage_Space);

                    int selectedId21 = rbsecurity.getCheckedRadioButtonId();
                    btn_security = findViewById(selectedId21);
                    String Security = btn_security.getText().toString().trim();
                    Log.v("tagvv", " " + Security);

                    int selectedId22 = rbelevator.getCheckedRadioButtonId();
                    btn_elevator = findViewById(selectedId22);
                    String Elevator = btn_elevator.getText().toString().trim();
                    Log.v("tagvv", " " + Elevator);

                    int selectedId23 = rbwheelchair.getCheckedRadioButtonId();
                    btn_wheelchair = findViewById(selectedId23);
                    String Wheelchair = btn_wheelchair.getText().toString().trim();
                    Log.v("tagvv", " " + Wheelchair);

                    int selectedId24 = rblabels.getCheckedRadioButtonId();
                    btn_labels = findViewById(selectedId24);
                    String Barille_Labels = btn_labels.getText().toString().trim();
                    Log.v("tagvv", " " + Barille_Labels);

                    int selectedId25 = rbaudio.getCheckedRadioButtonId();
                    btn_audio = findViewById(selectedId25);
                    String Audio = btn_audio.getText().toString().trim();
                    Log.v("tagvv", " " + Audio);

                    int selectedId26 = rbbicycle.getCheckedRadioButtonId();
                    btn_bicycle = findViewById(selectedId26);
                    String Bicycle = btn_bicycle.getText().toString().trim();
                    Log.v("tagvv", " " + Bicycle);

                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String uid = firebaseUser.getUid();
                    Log.v("tagvv", " " + uid);

                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("UserID", uid);
                    userMap.put("Title", Title);
                    userMap.put("Description", Description);
                    userMap.put("Amount", Amount);
                    userMap.put("Unit", Unit);
                    userMap.put("PhoneNumber", PhoneNumber);
                    userMap.put("MoveInDate", MoveInDate);
                    userMap.put("Furnished", Furnished);
                    userMap.put("UnitLaundry", UnitLaundry);
                    userMap.put("BuildingLaundry", BuildingLaundry);
                    userMap.put("Bathroom", Bathroom);
                    userMap.put("Dishwasher", Dishwasher);
                    userMap.put("Fridge", Fridge);
                    userMap.put("AirConditioning", AirConditioning);
                    userMap.put("Bedroom", Bedroom);
                    userMap.put("Yard", Yard);
                    userMap.put("PetFriendly", PetFriendly);
                    userMap.put("Balcony", Balcony);
                    userMap.put("Gym", Gym);
                    userMap.put("Pool", Pool);
                    userMap.put("Concierge", Concierge);
                    userMap.put("Size", Size);
                    userMap.put("Barrier_free_Entrance_Ramps", Barrier_free_Entrance_Ramps);
                    userMap.put("VisualAids", VisualAids);
                    userMap.put("Accessible_Washrooms_in_suite", Accessible_Washrooms_in_suite);
                    userMap.put("Storage_Space", Storage_Space);
                    userMap.put("24_Hour_Security", Security);
                    userMap.put("SmokePermitted", SmokePermitted);
                    userMap.put("Hydro", Hydro);
                    userMap.put("Heat", Heat);
                    userMap.put("Water", Water);
                    userMap.put("Tv", Tv);
                    userMap.put("Internet", Internet);
                    userMap.put("Elevator", Elevator);
                    userMap.put("Wheelchair_Accessible", Wheelchair);
                    userMap.put("Braille_Labels", Barille_Labels);
                    userMap.put("Audio_Prompts", Audio);
                    userMap.put("ParkingIncluded", ParkingIncluded);
                    userMap.put("Bicycle_Parking", Bicycle);
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
                        String Amount1 = data1.get("Amount").toString();
                        String Address1 = data1.get("Address").toString();
                        String CityName1 = data1.get("CityName").toString();
                        String Unit1 = data1.get("Unit").toString();
                        String PhoneNumber1 = data1.get("PhoneNumber").toString();
                        String MoveInDate1 = data1.get("MoveInDate").toString();
                        String Furnished1 = data1.get("Furnished").toString();
                        String UnitLaundry1 = data1.get("UnitLaundry").toString();
                        String BuildingLaundry1 = data1.get("BuildingLaundry").toString();
                        String Bathroom1 = data1.get("Bathroom").toString();
                        String Dishwasher1 = data1.get("Dishwasher").toString();
                        String Fridge1 = data1.get("Fridge").toString();
                        String AirConditioning1 = data1.get("AirConditioning").toString();
                        String Bedroom1 = data1.get("Bedroom").toString();
                        String Yard1 = data1.get("Yard").toString();
                        String PetFriendly1 = data1.get("PetFriendly").toString();
                        String Balcony1 = data1.get("Balcony").toString();
                        String Gym1 = data1.get("Gym").toString();
                        String Pool1 = data1.get("Pool").toString();
                        String Concierge1 = data1.get("Concierge").toString();
                        String Size1 = data1.get("Size").toString();
                        String Barrier_free_Entrance_Ramps1 = data1.get("Barrier_free_Entrance_Ramps").toString();
                        String VisualAids1 = data1.get("VisualAids").toString();
                        String Accessible_Washrooms_in_suite1 = data1.get("Accessible_Washrooms_in_suite").toString();
                        String Storage_Space1 = data1.get("Storage_Space").toString();
                        String Hour_Security1 = data1.get("24_Hour_Security").toString();
                        String SmokePermitted1 = data1.get("SmokePermitted").toString();
                        String Hydro1 = data1.get("Hydro").toString();
                        String Heat1 = data1.get("Heat").toString();
                        String Water1 = data1.get("Water").toString();
                        String Tv1 = data1.get("Tv").toString();
                        String Internet1 = data1.get("Internet").toString();
                        String Elevator1 = data1.get("Elevator").toString();
                        String Wheelchair_Accessible1 = data1.get("Wheelchair_Accessible").toString();
                        String Braille_Labels1 = data1.get("Braille_Labels").toString();
                        String Audio_Prompts1 = data1.get("Audio_Prompts").toString();
                        String ParkingIncluded1 = data1.get("ParkingIncluded").toString();
                        String Bicycle_Parking1 = data1.get("Bicycle_Parking").toString();
                        String Status = data1.get("Status").toString();
                        double latitude = (Double) data1.get("Latitude");
                        double longitude = (Double) data1.get("Longitude");
                        latLng = new LatLng(latitude, longitude);
                        cityName = CityName1;
                        address = Address1;


                        et_title.getEditText().setText(Title1);
                        et_des.getEditText().setText(Description1);
                        et_amt.getEditText().setText(Amount1);
                        autocompleteFragment.setText(Address1);
                        city.setText(CityName1);
                        et_unit.getEditText().setText(Unit1);
                        et_pnum.getEditText().setText(PhoneNumber1);
                        et_date.getEditText().setText(MoveInDate1);
                        et_bath.getEditText().setText(Bathroom1);

                        et_bed.getEditText().setText(Bedroom1);
                        et_pet.getEditText().setText(PetFriendly1);
                        et_size.getEditText().setText(Size1);
                        et_smoke.getEditText().setText(SmokePermitted1);
                        et_parking.getEditText().setText(ParkingIncluded1);

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
                        if (Furnished1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.Fyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.Fno);
                            rb2.setChecked(true);
                        }

                        if (UnitLaundry1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.layes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.lano);
                            rb2.setChecked(true);
                        }

                        if (BuildingLaundry1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.Laundrybyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.Laundrybno);
                            rb2.setChecked(true);
                        }

                        if (Dishwasher1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.dyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.dno);
                            rb2.setChecked(true);
                        }

                        if (Fridge1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.fryes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.frno);
                            rb2.setChecked(true);
                        }

                        if (AirConditioning1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.ayes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.ano);
                            rb2.setChecked(true);
                        }

                        if (Yard1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.yyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.yno);
                            rb2.setChecked(true);
                        }

                        if (Balcony1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.balyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.balno);
                            rb2.setChecked(true);
                        }

                        if (Gym1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.gymyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.gymno);
                            rb2.setChecked(true);
                        }

                        if (Pool1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.poolyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.poolno);
                            rb2.setChecked(true);
                        }

                        if (Concierge1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.conciergeyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.conciergeno);
                            rb2.setChecked(true);
                        }

                        if (Barrier_free_Entrance_Ramps1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.rampyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.rampno);
                            rb2.setChecked(true);
                        }

                        if (VisualAids1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.aidsyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.aidsno);
                            rb2.setChecked(true);
                        }

                        if (Accessible_Washrooms_in_suite1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.suiteyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.suiteno);
                            rb2.setChecked(true);
                        }

                        if (Storage_Space1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.storageyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.storageno);
                            rb2.setChecked(true);
                        }

                        if (Hour_Security1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.securityyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.securityno);
                            rb2.setChecked(true);
                        }

                        if (Hydro1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.hydroyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.hydrono);
                            rb2.setChecked(true);
                        }

                        if (Heat1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.heatyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.heatno);
                            rb2.setChecked(true);
                        }

                        if (Water1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.wateryes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.waterno);
                            rb2.setChecked(true);
                        }

                        if (Tv1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.tvyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.tvno);
                            rb2.setChecked(true);
                        }

                        if (Internet1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.internetyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.internetno);
                            rb2.setChecked(true);
                        }

                        if (Elevator1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.elevatoryes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.elevatorno);
                            rb2.setChecked(true);
                        }

                        if (Wheelchair_Accessible1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.wheelchairyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.wheelchairno);
                            rb2.setChecked(true);
                        }

                        if (Braille_Labels1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.labelsyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.labelsno);
                            rb2.setChecked(true);
                        }

                        if (Audio_Prompts1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.audioyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.audiono);
                            rb2.setChecked(true);
                        }

                        if (Bicycle_Parking1.equals("Yes")) {
                            RadioButton rb1 = findViewById(R.id.bicycleyes);
                            rb1.setChecked(true);
                        } else {
                            RadioButton rb2 = findViewById(R.id.bicycleno);
                            rb2.setChecked(true);
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