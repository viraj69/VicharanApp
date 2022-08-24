package com.example.aksharSparsh;

import android.app.Application;

public class GlobalApplication extends Application {
    private static GlobalApplication singleton;

    private boolean isGujaratiLanguageSelected = false;

    public static GlobalApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
    }

    public boolean isGujaratiLanguageSelected() {
        return isGujaratiLanguageSelected;
    }

    public void setGujaratiLanguageSelected(boolean gujaratiLanguageSelected) {
        isGujaratiLanguageSelected = gujaratiLanguageSelected;
    }
}