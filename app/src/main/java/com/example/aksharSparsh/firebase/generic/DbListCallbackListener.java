package com.example.aksharSparsh.firebase.generic;

import java.util.List;

public interface DbListCallbackListener<T> {
    void onDbListCallback(List<T> t);
}
