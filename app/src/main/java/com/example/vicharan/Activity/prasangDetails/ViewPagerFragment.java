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

import com.chahinem.pageindicator.PageIndicator;
import com.example.vicharan.Activity.prasangDetails.adapter.ViewImagePagerAdapter;
import com.example.vicharan.GlobalApplication;
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
    private ViewPager imageViewPager;
    private PageIndicator pageIndicator;
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
        return inflater.inflate(R.layout.fragment_prasang_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = view.findViewById(R.id.title);
        ((TextView) getActivity().findViewById(R.id.toolbar_title)).setText(locationPrasangPair.getLocation().getEnglishVersion().getPlace());
        place = view.findViewById(R.id.place);
        des = view.findViewById(R.id.des);
        date = view.findViewById(R.id.date);
        sutra = view.findViewById(R.id.sutra);
        imageViewPager = view.findViewById(R.id.imageslider);
        imageViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewImagePagerAdapter = new ViewImagePagerAdapter(getActivity());
        imageViewPager.setAdapter(viewImagePagerAdapter);
        pageIndicator = view.findViewById(R.id.pageIndicator);

        loadLocation(locationPrasangPair.getLocation());
        loadPrasang(locationPrasangPair.getPrasang());
        getImages(locationPrasangPair.getPrasang());
    }

    private void getImages(final Prasang prasang) {
        for (int i = 0; i < prasang.getMedia().size(); i++) {
            String mediaId = prasang.getMedia().get(i);
            DbMedia.getById(mediaId, (Media media) -> {
                if (media == null) return;
                FirebaseUtils.loadImage(prasang.getId(), media.getName(), (item) -> {
                    viewImagePagerAdapter.addItem(item);
                    pageIndicator.attachTo(imageViewPager);
                }, e -> {
                    System.out.println("Error downloading media file: " + e.getMessage());
                });
            });
        }
    }

    private void loadLocation(Location location) {
        boolean isGujaratiLanguageSelected = GlobalApplication.getInstance().isGujaratiLanguageSelected();
        if (isGujaratiLanguageSelected) {
            place.setText(location.getGujaratiVersion().getPlace());
        } else {
            place.setText(location.getEnglishVersion().getPlace());
        }
    }

    private void loadPrasang(Prasang prasang) {
        date.setText(prasang.getDate());
        boolean isGujaratiLanguageSelected = GlobalApplication.getInstance().isGujaratiLanguageSelected();
        if (isGujaratiLanguageSelected) {
            title.setText(prasang.getGujaratiVersion().getTitle());
            des.setText(prasang.getGujaratiVersion().getDescription());
            sutra.setText(prasang.getGujaratiVersion().getSutra());
        } else {
            title.setText(prasang.getEnglishVersion().getTitle());
            des.setText(prasang.getEnglishVersion().getDescription());
            sutra.setText(prasang.getEnglishVersion().getSutra());
        }
    }
}