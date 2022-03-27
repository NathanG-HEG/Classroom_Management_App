package com.hevs.classroom_management_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

public class ChangePwd extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private TeacherRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        repo = TeacherRepository.getInstance();

        Button changePwd = (Button) findViewById(R.id.change_pwd_btn2);
        changePwd.setOnClickListener(view -> {
            changePwd();
        });
    }

    private void changePwd(){
        repo.getById(sharedPref.getLong(MainActivity.ID_TEACHER, 0L), this).observe(this, teacher -> {

        });
    }


}
