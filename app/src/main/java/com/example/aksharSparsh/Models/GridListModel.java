package com.example.aksharSparsh.Models;

import com.example.aksharSparsh.R;

import java.util.ArrayList;

public class GridListModel {

    public ArrayList<GridModel> setListData() {
        ArrayList<GridModel> list = new ArrayList<>();
        list.add(new GridModel(R.drawable.marker1));
        list.add(new GridModel(R.drawable.marker1));

        return list;
    }
}
