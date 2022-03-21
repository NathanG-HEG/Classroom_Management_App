package com.hevs.classroom_management_app.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.R;

public class Settings extends AppCompatActivity {

    private boolean isNightMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        FloatingActionButton nightMode = (FloatingActionButton)  findViewById(R.id.night_mode_button);
        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    isNightMode = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    isNightMode = true;
                }
            }
        });
    }
}
