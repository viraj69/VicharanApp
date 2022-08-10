package com.example.vicharan.firebase.generic;

public interface DbInsertionListener {
    void onSuccess(String id);

    void onFailure(Exception e);
}