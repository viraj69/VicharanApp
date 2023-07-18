package com.example.aksharSparsh.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.aksharSparsh.DynamicGridViewActivity;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;

import java.util.LinkedList;

public class GridViewActivity extends AppCompatActivity {

    private DynamicGridViewActivity dynamicGridViewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        dynamicGridViewActivity = new DynamicGridViewActivity();

        // Add a new item to the GridView
        dynamicGridViewActivity.addItemToGridView(R.drawable.logo);

        // Remove an item from the GridView (position indicates the index of the item to be removed)
        dynamicGridViewActivity.removeItemFromGridView(0);

        // Use the dynamicGridViewActivity as needed
    }

    public static void startActivity(Activity caller, LinkedList<DbPrasang.LocationPrasangPair> locationPrasangPairList) {
        Intent i = new Intent(caller, GridViewActivity.class);
        i.putExtra("key", locationPrasangPairList);
        caller.startActivity(i);
    }
}