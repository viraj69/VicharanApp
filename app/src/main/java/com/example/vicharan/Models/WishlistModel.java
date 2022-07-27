package com.example.vicharan.Models;

import android.net.Uri;

public class WishlistModel {
    String apartmentId;
    String price;
    String bedroom;
    String bathroom;
    String location;
    String type;
    private Uri image;



    public WishlistModel(String apartmentId, String price, String bedroom, String bathroom, String location, String type, Uri image){
        this.apartmentId=apartmentId;
        this.price=price;
        this.bedroom=bedroom;
        this.bathroom=bathroom;
        this.location=location;
        this.image=image;
        this.type=type;
    }


    public String getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(String apartmentId) {
        this.apartmentId = apartmentId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
