package com.hevs.classroom_management_app.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hevs.classroom_management_app.R;

public class ClassroomDetails extends AppCompatActivity {

    public static final String ID_CLASSROOM = "idClassroom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_classroom);
    }
}
