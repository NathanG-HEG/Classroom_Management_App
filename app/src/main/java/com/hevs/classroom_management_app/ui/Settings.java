package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.R;

public class Settings extends AppCompatActivity {

    public static final String THEME_PREFERENCE = "theme_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //toggle night theme button
        FloatingActionButton nightMode = (FloatingActionButton) findViewById(R.id.night_mode_button);
        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleTheme();
            }
        });

        //about button
        FloatingActionButton about = (FloatingActionButton) findViewById(R.id.about_button);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, About.class);
                startActivity(i);
            }
        });
    }

    private void toggleTheme() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        boolean isNightMode = sharedPref.getBoolean(THEME_PREFERENCE, true);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            editor.putBoolean(THEME_PREFERENCE, false);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            editor.putBoolean(THEME_PREFERENCE, true);
        }
        editor.commit();
    }
}
