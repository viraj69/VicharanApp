package com.example.vicharan.Activity.prasangDetails;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.vicharan.Activity.prasangDetails.adapter.ViewImagePagerAdapter;
import com.example.vicharan.R;
import com.example.vicharan.firebase.FirebaseUtils;
import com.example.vicharan.firebase.location.Location;
import com.example.vicharan.firebase.media.DbMedia;
import com.example.vicharan.firebase.media.Media;
import com.example.vicharan.firebase.prasang.Prasang;

public class PrasangDetails extends AppCompatActivity {
    private static final String LOCATION_INTENT_KEY = "location";
    private static final String PRASANG_INTENT_KEY = "prasang";
    private static final String MEDIA_INTENT_KEY = "media";

    private TextView title, place, des, date, sutra;
    private ViewImagePagerAdapter viewImagePagerAdapter;

    private Location location;
    private Prasang prasang;
    private Media media;

    public static void startActivity(Activity caller, Location location, Prasang prasang, Media media) {
        Intent i = new Intent(caller, PrasangDetails.class);
        i.putExtra(LOCATION_INTENT_KEY, location);
        i.putExtra(PRASANG_INTENT_KEY, prasang);
        i.putExtra(MEDIA_INTENT_KEY, media);
        caller.startActivity(i);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        location = (Location) intent.getSerializableExtra(LOCATION_INTENT_KEY);
        prasang = (Prasang) intent.getSerializableExtra(PRASANG_INTENT_KEY);
        media = (Media) intent.getSerializableExtra(MEDIA_INTENT_KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);
        parseIntent();

        title = findViewById(R.id.title);
        place = findViewById(R.id.place);
        des = findViewById(R.id.des);
        date = findViewById(R.id.date);
        sutra = findViewById(R.id.sutra);
        ViewPager imageViewPager = findViewById(R.id.imageslider);
        viewImagePagerAdapter = new ViewImagePagerAdapter(getApplicationContext());
        imageViewPager.setAdapter(viewImagePagerAdapter);

        loadLocation(location);
        loadPrasang(prasang);
        getImages(prasang);
    }

    private void getImages(final Prasang prasang) {
        for (String mediaId : prasang.getMedia()) {
            DbMedia.getById(mediaId, (Media media) -> {
                if (media == null) return;
                FirebaseUtils.loadImage(prasang.getId(), media.getName(), viewImagePagerAdapter::addItem, e -> {
                    System.out.println("Error downloading media file: " + e.getMessage());
                });
            });
        }
    }

    private void loadLocation(Location location) {
        place.setText(location.getPlace());
    }

    private void loadPrasang(Prasang prasang) {
        title.setText(prasang.getTitle());
        des.setText(prasang.getDescription());
        date.setText(prasang.getDate());
        sutra.setText(prasang.getSutra());
    }
}