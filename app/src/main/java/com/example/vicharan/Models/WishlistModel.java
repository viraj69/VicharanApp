package com.example.vicharan.Models;

import android.net.Uri;

public class WishlistModel {
    String apartmentId;
    String place;
    String location;
    String sutra;
    private Uri image;


    public WishlistModel(String apartmentId, String place, String location, String sutra, Uri image) {
        this.apartmentId = apartmentId;
        this.place = place;
        this.location = location;
        this.image = image;
        this.sutra = sutra;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public void setSutra(String sutra) {
        this.sutra = sutra;
    }
}
