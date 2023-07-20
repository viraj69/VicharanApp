package com.example.aksharSparsh.Models;

public class GridModel {

    private int image;

    String title;

//    public GridModel(int image, String title) {
//        this.image = image;
//        this.title = title;
//    }

    public GridModel(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
