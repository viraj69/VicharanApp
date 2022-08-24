package com.example.aksharSparsh.Activity.prasangDetails;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.chahinem.pageindicator.PageIndicator;
import com.example.aksharSparsh.Activity.prasangDetails.adapter.ViewImagePagerAdapter;
import com.example.aksharSparsh.GlobalApplication;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.FirebaseUtils;
import com.example.aksharSparsh.firebase.location.Location;
import com.example.aksharSparsh.firebase.media.DbMedia;
import com.example.aksharSparsh.firebase.media.Media;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;
import com.example.aksharSparsh.firebase.prasang.Prasang;

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
            Typeface europaregular = ResourcesCompat.getFont(getActivity(), R.font.europaregular);
            place.setTypeface(europaregular);
            place.setText(Html.fromHtml(location.getGujaratiVersion().getPlace()));
        } else {
            Typeface europaRegular = ResourcesCompat.getFont(getActivity(), R.font.europaregular);
            place.setTypeface(europaRegular);
            place.setText(Html.fromHtml(location.getEnglishVersion().getPlace()));
        }
    }

    private void loadPrasang(Prasang prasang) {
        date.setText(Html.fromHtml(prasang.getDate()));
        boolean isGujaratiLanguageSelected = GlobalApplication.getInstance().isGujaratiLanguageSelected();
        if (isGujaratiLanguageSelected) {

            Typeface kap026 = ResourcesCompat.getFont(getActivity(), R.font.kap026);

            title.setTypeface(kap026);
            title.setText(Html.fromHtml(prasang.getGujaratiVersion().getTitle()));
            des.setTypeface(kap026);
            des.setText(Html.fromHtml(prasang.getGujaratiVersion().getDescription()));
            sutra.setTypeface(kap026);
            sutra.setText(Html.fromHtml(prasang.getGujaratiVersion().getSutra()));
            date.setTypeface(kap026);
        } else {

            Typeface berkshireswashRegular = ResourcesCompat.getFont(getActivity(), R.font.berkshireswash_regular);
            Typeface europaRegular = ResourcesCompat.getFont(getActivity(), R.font.europaregular);

            title.setText(Html.fromHtml(prasang.getEnglishVersion().getTitle()));
            title.setTypeface(berkshireswashRegular);
            des.setText(Html.fromHtml(prasang.getEnglishVersion().getDescription()));
            des.setTypeface(europaRegular);
            sutra.setText(Html.fromHtml(prasang.getEnglishVersion().getSutra()));
            sutra.setTypeface(europaRegular);
            date.setTypeface(europaRegular);
        }
    }
}