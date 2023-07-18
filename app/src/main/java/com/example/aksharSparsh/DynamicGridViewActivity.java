package com.example.aksharSparsh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aksharSparsh.Activity.prasangDetails.PrasangDetails;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DynamicGridViewActivity extends AppCompatActivity {
    private List<Integer> imageList = new ArrayList<>();
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_grid_view);

        gridLayout = findViewById(R.id.gridLayout);
    }

    // Method to add a new item to the GridView
    public void addItemToGridView(int imageResource) {
        imageList.add(imageResource);
        refreshGridView();
    }

    // Method to remove an item from the GridView
    public void removeItemFromGridView(int position) {
        if (position >= 0 && position < imageList.size()) {
            imageList.remove(position);
            refreshGridView();
        }
    }

    // Method to refresh the GridView and apply animations
    private void refreshGridView() {
        gridLayout.removeAllViews();

        for (int i = 0; i < imageList.size(); i++) {
            View gridItem = getLayoutInflater().inflate(R.layout.item_grid_animated, gridLayout, false);
            ImageView imageView = gridItem.findViewById(R.id.imageView);
            imageView.setImageResource(imageList.get(i));

            // Apply animation to each grid item
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.grid_item_anim);
            gridItem.startAnimation(animation);

            gridLayout.addView(gridItem);
        }
    }
}