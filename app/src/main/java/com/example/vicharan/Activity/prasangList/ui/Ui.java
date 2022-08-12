package com.example.vicharan.Activity.prasangList.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vicharan.Activity.prasangList.adapter.PrasangListAdapter;
import com.example.vicharan.R;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.example.vicharan.generic.UiService;

import java.util.LinkedList;
import java.util.List;

public class Ui implements UiService {
    private final AppCompatActivity appCompatActivity;
    private final PrasangListAdapter postlistAdapter;
    private final PrasangListItemUi.OnPrasangClickListener prasangClickListener;

    public Ui(AppCompatActivity appCompatActivity, PrasangListItemUi.OnPrasangClickListener prasangClickListener) {
        this.appCompatActivity = appCompatActivity;
        this.prasangClickListener = prasangClickListener;
        this.postlistAdapter = new PrasangListAdapter(appCompatActivity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onViewCreated(View v) {
        RecyclerView PostListRecycler = appCompatActivity.findViewById(R.id.postlist_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(appCompatActivity, RecyclerView.VERTICAL, false);
        PostListRecycler.setLayoutManager(layoutManager);
        PostListRecycler.setAdapter(postlistAdapter);
    }

    @Override
    public void init() {
    }

    @Override
    public void onViewDestroyed() {
    }

    @Override
    public void onClick(View view) {
    }

    public void setList(List<DbPrasang.LocationPrasangPair> prasangList) {
        List<PrasangListItemUi> prasangListItemUis = new LinkedList<>();
        for (DbPrasang.LocationPrasangPair locationPrasangPair : prasangList) {
            prasangListItemUis.add(new PrasangListItemUi(locationPrasangPair, appCompatActivity, prasangClickListener));
        }
        postlistAdapter.setData(prasangListItemUis);
    }

    public void clearList() {
        postlistAdapter.clear();
    }
}
