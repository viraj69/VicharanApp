package com.example.vicharan.Models;

import android.net.Uri;

public class PostListModel {
    String apartmentId;
    String place;
    String title;
    String sutra;
    private Uri image;


    public PostListModel(String apartmentId, String title, String place, String sutra, Uri image) {
        this.apartmentId = apartmentId;
        this.place = place;
        this.image = image;
        this.sutra = sutra;
        this.title = title;
    }


    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public String getSutra() {
        return sutra;
    }

    public void setSutra(String type) {
        this.sutra = sutra;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
