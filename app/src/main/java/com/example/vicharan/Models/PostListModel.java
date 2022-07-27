package com.example.vicharan.Models;

import android.net.Uri;

public class PostListModel {
    String apartmentId;
    String price;
    String bedroom;
String title;

    String type;
    private Uri image;



    public PostListModel(String apartmentId,String title, String price, String bedroom, String type, Uri image){
        this.apartmentId=apartmentId;
        this.price=price;
        this.bedroom=bedroom;
        this.image=image;
        this.type=type;
        this.title=title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
