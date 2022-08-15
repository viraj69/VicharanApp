package com.example.vicharan.Activity.prasangDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.vicharan.Activity.prasangDetails.adapter.ViewImagePagerAdapter;
import com.example.vicharan.R;
import com.example.vicharan.firebase.FirebaseUtils;
import com.example.vicharan.firebase.location.Location;
import com.example.vicharan.firebase.media.DbMedia;
import com.example.vicharan.firebase.media.Media;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.example.vicharan.firebase.prasang.Prasang;

public class ViewPagerFragment extends Fragment {
    private DbPrasang.LocationPrasangPair locationPrasangPair;

    private TextView title, place, des, date, sutra;
    private ViewImagePagerAdapter viewImagePagerAdapter;

    public void setData(DbPrasang.LocationPrasangPair locationPrasangPair) {
        this.locationPrasangPair = locationPrasangPair;
    }

    public ViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_viewpager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = view.findViewById(R.id.title);
        place = view.findViewById(R.id.place);
        des = view.findViewById(R.id.des);
        date = view.findViewById(R.id.date);
        sutra = view.findViewById(R.id.sutra);
        ViewPager imageViewPager = view.findViewById(R.id.imageslider);
        viewImagePagerAdapter = new ViewImagePagerAdapter(getActivity());
        imageViewPager.setAdapter(viewImagePagerAdapter);

        loadLocation(locationPrasangPair.getLocation());
        loadPrasang(locationPrasangPair.getPrasang());
        getImages(locationPrasangPair.getPrasang());
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