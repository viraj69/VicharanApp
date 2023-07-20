package com.example.aksharSparsh.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.aksharSparsh.Adapters.GridListAdapters;
import com.example.aksharSparsh.DynamicGridViewActivity;
import com.example.aksharSparsh.Models.GridListModel;
import com.example.aksharSparsh.Models.GridModel;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;

import java.util.ArrayList;
import java.util.LinkedList;

public class GridViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private DynamicGridViewActivity dynamicGridViewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        GridView grid = findViewById(R.id.gridview);
        GridListAdapters gridviewAdapter = new GridListAdapters(GridViewActivity.this,new GridListModel().setListData());
        grid.setAdapter(gridviewAdapter);
        grid.setOnItemClickListener(this);


//        dynamicGridViewActivity = new DynamicGridViewActivity();
//
//        // Add a new item to the GridView
//        dynamicGridViewActivity.addItemToGridView(R.drawable.logo);
//
//        // Remove an item from the GridView (position indicates the index of the item to be removed)
//        dynamicGridViewActivity.removeItemFromGridView(0);
//
//        // Use the dynamicGridViewActivity as needed
    }

    public static void startActivity(Activity caller, LinkedList<DbPrasang.LocationPrasangPair> locationPrasangPairList) {
        Intent i = new Intent(caller, GridViewActivity.class);
        i.putExtra("key", locationPrasangPairList);
        caller.startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
        GridModel G = (GridModel) parent.getItemAtPosition(position);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}