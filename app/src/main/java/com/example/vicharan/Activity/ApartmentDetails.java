package com.example.vicharan.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.vicharan.Adapters.ViewImagePagerAdapter;
import com.example.vicharan.Adapters.ViewPagerAdapter;
import com.example.vicharan.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class ApartmentDetails extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView title, place, address, sutra, des, date;
    LinearLayout mainLayout;
    ViewPager imageViewPager;
    FirebaseFirestore fstore;

    ProgressDialog pd;
    StorageReference storageReference;
    String UserId;
    String Uid;
    private FirebaseUser curUser;
    private FirebaseAuth auth;
    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewPager);
        title = (TextView) findViewById(R.id.title);
        address = (TextView) findViewById(R.id.address);
        sutra = (TextView) findViewById(R.id.sutra);
        date = (TextView) findViewById(R.id.date);
        des = (TextView) findViewById(R.id.des);
        place = (TextView) findViewById(R.id.place);
        imageViewPager = findViewById(R.id.imageslider);
        browser = (WebView) findViewById(R.id.browser2);
        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        browser.getSettings().setJavaScriptEnabled(true);

        ArrayList<Uri> images = new ArrayList<Uri>();

        auth = FirebaseAuth.getInstance();


        final String AptId;
        Bundle extras = getIntent().getExtras();
        AptId = extras.getString("AptId");
        curUser = auth.getCurrentUser();
        if (curUser != null) {
            UserId = curUser.getUid();
        }
        pd = new ProgressDialog(ApartmentDetails.this);
        pd.setMessage("Loading...");
        pd.show();

        getTabs(AptId);
        getImages(images, AptId);
        getdata(AptId);
        pd.dismiss();
    }

    private void getImages(final ArrayList<Uri> images, final String aptId) {
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child("images/" + aptId);
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
                            storageReference.child("images/" + aptId + "/" + image).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL for 'users/me/profile.png'
                                    System.out.println(images.size() + "maa");
                                    images.add(uri);
                                    ViewImagePagerAdapter viewImagePagerAdapter = new ViewImagePagerAdapter(getApplicationContext(), images);
                                    viewImagePagerAdapter.notifyDataSetChanged();
                                    imageViewPager.setAdapter(viewImagePagerAdapter);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }


    private void getdata(String aptId) {

        fstore = FirebaseFirestore.getInstance();
        DocumentReference docRef = fstore.collection("Apartment").document(aptId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> data1 = document.getData();

                        String title = data1.get("title").toString();
                        String place = data1.get("place").toString();
                        String des = data1.get("description").toString();
                        String address = data1.get("address").toString();
                        String date = data1.get("date").toString();
                        String sutra = data1.get("sutra").toString();
                        Uid = data1.get("userId").toString();
                        ApartmentDetails.this.title.setText(title);
                        ApartmentDetails.this.place.setText(place);
                        ApartmentDetails.this.address.setText(address);
                        ApartmentDetails.this.date.setText(date);
                        ApartmentDetails.this.des.setText(des);
                        ApartmentDetails.this.sutra.setText(sutra);

                        String lat = data1.get("latitude").toString();
                        String lng = data1.get("longitude").toString();
                        browser.loadUrl("file:///android_asset/local.html?lat=" + lat + "&lng=" + lng);

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


    public void getTabs(final String aptId) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                viewPager.setAdapter(viewPagerAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}