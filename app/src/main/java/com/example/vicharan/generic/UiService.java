package com.example.vicharan.generic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface UiService extends View.OnClickListener {
    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void onViewCreated(View v);

    void init();

    void onViewDestroyed();
}
