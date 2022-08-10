package com.example.vicharan.Activity.prasangList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vicharan.Activity.UpdateAd;
import com.example.vicharan.Activity.prasangList.ui.PrasangListItemUi;
import com.example.vicharan.Activity.prasangList.ui.Ui;
import com.example.vicharan.R;
import com.example.vicharan.firebase.location.DbLocation;
import com.example.vicharan.firebase.location.Location;
import com.example.vicharan.firebase.prasang.DbPrasang;
import com.example.vicharan.firebase.prasang.Prasang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class PrasangList extends AppCompatActivity implements PrasangListItemUi.OnPrasangClickListener {
    private final Ui ui = new Ui(this, this);

    public Ui getUi() {
        return ui;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);
        getUi().onViewCreated(null);
        init();
    }

    private void init() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser curUser = auth.getCurrentUser();
        String userId = null;
        if (curUser != null) {
            userId = curUser.getUid(); //Do what you need to do with the id
        }
        getPrasangList(userId);
    }

    private void getPrasangList(String userId) {
        DbPrasang.getByUserId(userId, (List<Prasang> prasangs) -> {
            if (prasangs == null || prasangs.isEmpty()) {
                System.out.println("no prasangs found!");
                return;
            } else {
                System.out.println("found prasangs: " + prasangs.size());
            }
            fetchLocations(prasangs);
        });
    }

    private void fetchLocations(List<Prasang> prasangs) {
        final HashMap<Prasang, Location> prasangLocationPair = new HashMap<>();
        for (int i = 0; i < prasangs.size(); i++) {
            final int index = i;
            final Prasang prasang = prasangs.get(i);

            DbLocation.getById(prasang.getLocationId(), (Location location) -> {
                if (location == null) return;
                prasangLocationPair.put(prasang, location);
                if (index == prasangs.size() - 1) {
                    onPrasangLocationPairAvailable(prasangLocationPair);
                }
            });
        }
    }

    void onPrasangLocationPairAvailable(HashMap<Prasang, Location> prasangLocationPair) {
        List<PrasangListItemUi.LocationPrasangPair> prasangPairLinkedList = new LinkedList<>();
        for (Prasang prasang : prasangLocationPair.keySet()) {
            prasangPairLinkedList.add(new PrasangListItemUi.LocationPrasangPair(prasangLocationPair.get(prasang), prasang));
        }
        getUi().setList(prasangPairLinkedList);
    }

    @Override
    public void onPrasangClick(PrasangListItemUi prasangListItemUi) {
        PrasangListItemUi.LocationPrasangPair locationPrasangPair = prasangListItemUi.getLocationPrasangPair();
        UpdateAd.startActivity(this, locationPrasangPair.getLocation(), locationPrasangPair.getPrasang());
    }
}