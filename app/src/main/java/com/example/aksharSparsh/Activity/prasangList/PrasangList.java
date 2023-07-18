package com.example.aksharSparsh.Activity.prasangList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aksharSparsh.Activity.prasangList.ui.PrasangListItemUi;
import com.example.aksharSparsh.Activity.prasangList.ui.Ui;
import com.example.aksharSparsh.Activity.updatePrasang.UpdatePrasang;
import com.example.aksharSparsh.R;
import com.example.aksharSparsh.firebase.prasang.DbPrasang;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrasangList extends AppCompatActivity implements PrasangListItemUi.OnPrasangClickListener {
    private final Ui ui = new Ui(this, this);

    public Ui getUi() {
        return ui;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prasang_list);
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
        DbPrasang.getPrasangsWithTheirLocationByUserId(userId, (getUi()::setList));
    }

    @Override
    public void onPrasangClick(PrasangListItemUi prasangListItemUi) {
        DbPrasang.LocationPrasangPair locationPrasangPair = prasangListItemUi.getLocationPrasangPair();
        UpdatePrasang.startActivity(this, locationPrasangPair.getLocation(), locationPrasangPair.getPrasang());
    }
}