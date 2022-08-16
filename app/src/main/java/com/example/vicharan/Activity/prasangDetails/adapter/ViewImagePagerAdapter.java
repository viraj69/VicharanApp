package com.example.vicharan.Activity.prasangDetails.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.vicharan.R;
import com.squareup.picasso.Picasso;

import java.util.LinkedList;
import java.util.List;

public class ViewImagePagerAdapter extends PagerAdapter {
    private final Context context;
    private List<Uri> images = new LinkedList<>();
    Animation animZoomIn;


    public ViewImagePagerAdapter(Context context) {
        this.context = context;
        this.images = images;
    }

    public void addItem(Uri uri) {
        images.add(uri);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.prasang_img, null);
        ImageView imageView = view.findViewById(R.id.img1);
        animZoomIn = AnimationUtils.loadAnimation(context.getApplicationContext(),
                R.anim.zoom_in);
        Picasso.get().load(images.get(position)).into(imageView);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(animZoomIn);
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
