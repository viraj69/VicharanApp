package com.example.aksharSparsh.Models;

import android.net.Uri;

public class GridListModel {

    private Uri image;

    String title;

    public GridListModel(String title, Uri image) {
        this.image = image;
        this.title = title;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
