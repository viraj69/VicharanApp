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
    private TextInputLayout et_title, et_des, et_amt, et_unit, et_pnum, et_date, et_bath, et_bed, et_pet, et_size, et_smoke, et_parking;
    private RadioGroup rbfurnished, rbflaundry, rbLaundryb, rbdishwasher, rbfridge, rbair_conditioning, rbyard, rbbalcony, rbramp, rbaids, rbsuite, rbhydro, rbheat, rbwater, rbtv, rbinternet, rbgym, rbpool, rbconcierge, rbstorage, rbsecurity, rbelevator, rbwheelchair, rblabels, rbaudio, rbbicycle;
    private RadioButton btn_flaundry, btn_furnished, btn_Laundryb, btn_dishwasher, btn_fridge, btn_air_conditioning, btn_yard, btn_balcony, btn_ramp, btn_aids, btn_suite, btn_hydro, btn_heat, btn_water, btn_tv, btn_internet, btn_gym, btn_pool, btn_concierge, btn_storage, btn_security, btn_elevator, btn_wheelchair, btn_labels, btn_audio, btn_bicycle;
    int photos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postadd);

        et_title = findViewById(R.id.title);
        et_des = findViewById(R.id.des);
        et_amt = findViewById(R.id.amount);
        et_unit = findViewById(R.id.unit1);
        et_pnum = findViewById(R.id.pnum);
        et_date = findViewById(R.id.date);
        et_bath = findViewById(R.id.bathroom1);
        et_bed = findViewById(R.id.bedroom1);
        et_pet = findViewById(R.id.pet1);
        et_size = findViewById(R.id.size);
        et_smoke = findViewById(R.id.smoke1);
        et_parking = findViewById(R.id.parking1);
        Button btn_calender = findViewById(R.id.calender);


        upload = findViewById(R.id.uploadImage);
        image = new ImageView[]{upload, selectedImage1, selectedImage2, selectedImage3};


        Button btn_postad = findViewById(R.id.post_ad);

        rbfurnished = findViewById(R.id.rbfurnished);
        rbflaundry = findViewById(R.id.rbflaundry);
        rbLaundryb = findViewById(R.id.rbLaundryb);
        rbdishwasher = findViewById(R.id.rbdishwasher);
        rbfridge = findViewById(R.id.rbfridge);
        rbair_conditioning = findViewById(R.id.rbair_conditioning);
        rbyard = findViewById(R.id.rbyard);
        rbbalcony = findViewById(R.id.rbbalcony);
        rbramp = findViewById(R.id.rbramp);
        rbaids = findViewById(R.id.rbaids);
        rbsuite = findViewById(R.id.rbsuite);
        rbhydro = findViewById(R.id.rbhydro);
        rbheat = findViewById(R.id.rbheat);
        rbwater = findViewById(R.id.rbwater);
        rbtv = findViewById(R.id.rbtv);
        rbinternet = findViewById(R.id.rbinternet);
        rbgym = findViewById(R.id.rbgym);
        rbpool = findViewById(R.id.rbpool);
        rbconcierge = findViewById(R.id.rbconcierge);
        rbstorage = findViewById(R.id.rbstorage);
        rbsecurity = findViewById(R.id.rbsecurity);
        rbelevator = findViewById(R.id.rbelevator);
        rbwheelchair = findViewById(R.id.rbwheelchair);
        rblabels = findViewById(R.id.rblabels);
        rbaudio = findViewById(R.id.rbaudio);
        rbbicycle = findViewById(R.id.rbbicycle);

        AutoCompleteTextView unit = findViewById(R.id.unit);
        AutoCompleteTextView bedroom = findViewById(R.id.bedroom);
        AutoCompleteTextView bathroom = findViewById(R.id.bathroom);
        AutoCompleteTextView pet = findViewById(R.id.pet);
        AutoCompleteTextView smoke = findViewById(R.id.smoke);
        AutoCompleteTextView parking = findViewById(R.id.parking);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.location_fragment);
        city = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.city);
        String[] units = new String[]{"Apartment", "Room", "House", "Condo"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(
                Postadd.this,
                R.layout.dropdown_item,
                units
        );

        unit.setAdapter(adapter1);


        String[] Bedrooms = new String[]{"1", "2", "3", "4", "5"};

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                Postadd.this,
                R.layout.dropdown_item,
                Bedrooms
        );

        bedroom.setAdapter(adapter2);

        String[] bathrooms = new String[]{"1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6"};

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(
                Postadd.this,
                R.layout.dropdown_item,
                bathrooms
        );

        bathroom.setAdapter(adapter3);


        String[] pets = new String[]{"Yes", "No", "Limited"};

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(
                Postadd.this,
                R.layout.dropdown_item,
                pets
        );

        pet.setAdapter(adapter4);


        String[] smokes = new String[]{"Yes", "No", "Outdoors only"};

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(
                Postadd.this,
                R.layout.dropdown_item,
                smokes
        );

        smoke.setAdapter(adapter5);

        String[] parkings = new String[]{"0", "1", "2", "3+"};

        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(
                Postadd.this,
                R.layout.dropdown_item,
                parkings
        );

        parking.setAdapter(adapter6);


        Calendar calender = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calender.clear();

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        calender.setTimeInMillis(today);

        final CalendarConstraints.Builder constraint = new CalendarConstraints.Builder();
        constraint.setValidator(DateValidatorPointForward.now());


        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();

        builder.setTitleText("SELECT A DATE");
        builder.setCalendarConstraints(constraint.build());
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
                } else if (Amount.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(Postadd.this, "Please Enter Amount in Digit", Toast.LENGTH_LONG).show();
                    return;
                }  else if (address == null) {
                    Toast.makeText(Postadd.this, "Please enter Address ", Toast.LENGTH_LONG).show();
                    return;
                } else if (cityName == null) {
                    Toast.makeText(Postadd.this, "Please select City", Toast.LENGTH_LONG).show();
                    return;
                } else if (Unit.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please select Unit", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Unit.equals("Apartment") || Unit.equals("Room") || Unit.equals("House") || Unit.equals("Condo"))) {
                    Toast.makeText(Postadd.this, "Please Select Unit from DropDown", Toast.LENGTH_LONG).show();
                    et_unit.getEditText().getText().clear();
                    return;
                } else if (PhoneNumber.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter PhoneNumber ", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(Postadd.this, "Please Enter PhoneNumber in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (PhoneNumber.length() < 10 || PhoneNumber.length() > 10) {
                    Toast.makeText(Postadd.this, "Please enter 10 to 12 digit PhoneNumber", Toast.LENGTH_LONG).show();
                    return;
                } else if (MoveInDate.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter MoveInDate ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Bathroom.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please select Bathroom", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Bathroom.equals("1") || Bathroom.equals("1.5") || Bathroom.equals("2") || Bathroom.equals("2.5") || Bathroom.equals("3") || Bathroom.equals("3.5") || Bathroom.equals("4") || Bathroom.equals("4.5") || Bathroom.equals("5") || Bathroom.equals("5.5") || Bathroom.equals("6"))) {
                    Toast.makeText(Postadd.this, "Please Select Bathroom from DropDown", Toast.LENGTH_LONG).show();
                    et_bath.getEditText().getText().clear();
                    return;
                } else if (Bedroom.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please select Bedroom", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(Bedroom.equals("1") || Bedroom.equals("2") || Bedroom.equals("3") || Bedroom.equals("4") || Bedroom.equals("5"))) {
                    Toast.makeText(Postadd.this, "Please Select Bedroom from DropDown", Toast.LENGTH_LONG).show();
                    et_bed.getEditText().getText().clear();
                    return;
                } else if (PetFriendly.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please select Pet Friendly", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(PetFriendly.equals("Yes") || PetFriendly.equals("No") || PetFriendly.equals("Limited"))) {
                    Toast.makeText(Postadd.this, "Please Select Pet Friendly from DropDown", Toast.LENGTH_LONG).show();
                    et_pet.getEditText().getText().clear();
                    return;
                } else if (Size.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please enter Size ", Toast.LENGTH_LONG).show();
                    return;
                } else if (Size.matches(".*[a-zA-Z]+.*")) {
                    Toast.makeText(Postadd.this, "Please Enter Size in Digit", Toast.LENGTH_LONG).show();
                    return;
                } else if (SmokePermitted.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please select Smoke Permitted", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(SmokePermitted.equals("Yes") || SmokePermitted.equals("No") || SmokePermitted.equals("Outdoors only"))) {
                    Toast.makeText(Postadd.this, "Please Select Smoke Permitted from DropDown", Toast.LENGTH_LONG).show();
                    et_smoke.getEditText().getText().clear();
                    return;
                } else if (ParkingIncluded.isEmpty()) {
                    Toast.makeText(Postadd.this, "Please select Parking Included", Toast.LENGTH_LONG).show();
                    return;
                } else if (!(ParkingIncluded.equals("0") || ParkingIncluded.equals("1") || ParkingIncluded.equals("2") || ParkingIncluded.equals("3+"))) {
                    Toast.makeText(Postadd.this, "Please Select Parking Included from DropDown", Toast.LENGTH_LONG).show();
                    et_parking.getEditText().getText().clear();
                    return;

                } else if (photos < 1) {
                    Toast.makeText(Postadd.this, "Please Select atleast 1 photo", Toast.LENGTH_LONG).show();
                } else {
                    final ProgressDialog pd;
                    pd = new ProgressDialog(Postadd.this);
                    pd.setMessage("Loading...");
                    pd.show();
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