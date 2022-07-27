package com.example.vicharan.Activity;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vicharan.R;

public class localcontent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localcontent);
        //   Webview browser=(Webview) findViewById(R.id.browser); //if you gave the id as browser
        WebView browser = (WebView) findViewById(R.id.browser);
        browser.getSettings().setJavaScriptEnabled(true); //Yes you have to do it
        browser.loadUrl("file:///android_asset/local.html"); //If you put the HTML file in asset folder of android
    }
}