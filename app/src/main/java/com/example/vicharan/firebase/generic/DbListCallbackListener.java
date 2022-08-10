package com.example.vicharan.firebase.generic;

import java.util.List;

public interface DbListCallbackListener<T> {
    void onDbListCallback(List<T> t);
}
