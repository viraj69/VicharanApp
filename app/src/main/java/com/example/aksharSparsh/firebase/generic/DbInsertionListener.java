package com.example.aksharSparsh.firebase.generic;

public interface DbInsertionListener {
    void onSuccess(String id);

    void onFailure(Exception e);
}