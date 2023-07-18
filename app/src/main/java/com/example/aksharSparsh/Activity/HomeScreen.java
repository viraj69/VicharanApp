package com.example.aksharSparsh.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aksharSparsh.R;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ImageView First = (ImageView) findViewById(R.id.HariprasadSwami);
        ImageView second = (ImageView) findViewById(R.id.PrabodhSwami);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String tag1 = "swamiji";
        String tag2 = "prabodhSwamiji";
        First.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("key", tag1);
                editor.apply();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("key", tag2);
                editor.apply();
                Intent intent1 = new Intent(view.getContext(), MainActivity.class);

                startActivity(intent1);
            }
        });

    }
}