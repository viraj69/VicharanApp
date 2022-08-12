package com.example.vicharan.Activity.prasangList.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.vicharan.R;
import com.example.vicharan.firebase.FirebaseUtils;
import com.example.vicharan.firebase.media.DbMedia;
import com.example.vicharan.firebase.media.Media;
import com.example.vicharan.firebase.prasang.Prasang;
import com.example.vicharan.generic.UiService;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PrasangMediaUi implements UiService {
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private final Prasang prasang;

    private ImageView prasangImageView;

    public PrasangMediaUi(Prasang prasang) {
        this.prasang = prasang;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // do nothing
        return null;
    }

    @Override
    public void onViewCreated(View v) {
        prasangImageView = v.findViewById(R.id.postlist_image);
    }

    @Override
    public void init() {
        String firstMediaId = prasang.getMedia().get(0);
        DbMedia.getById(firstMediaId, (Media media) -> {
            if (media == null) return;
            FirebaseUtils.loadImage(prasang.getId(), media.getName(), this::showImage, this::onErrorLoadingImage);
        });
    }

    private void showImage(Uri uri) {
        Picasso.get().load(uri).fit().into(prasangImageView);
    }

    private void onErrorLoadingImage(Exception e) {

    }

    @Override
    public void onViewDestroyed() {
        // Do nothing
    }

    @Override
    public void onClick(View view) {
        // Do nothing
    }
}
