package com.hevs.classroom_management_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

public class MainActivity extends AppCompatActivity {

    public static final String ID_TEACHER = "idTeacher";
    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teacherRepository = ((BaseApp) getApplication()).getTeacherRepository();

        //login click listener
        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(MainActivity.this);
            }
        });

        //sign up click listener
        Button signUp = findViewById(R.id.signUp_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(MainActivity.this);
            }
        });

    }

    private void login(AppCompatActivity parent) {
        EditText emailEt = (EditText) findViewById(R.id.editTextTextEmailAddress);
        String email = emailEt.getText().toString();
        String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();

        teacherRepository.getByLogin(email, password, getApplication()).observe(MainActivity.this, teacher -> {

            if (teacher != null) {
                Intent i = new Intent(parent, ClassroomListActivity.class);
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(ID_TEACHER, teacher.getId());
                editor.commit();
                startActivity(i);
            } else {
                emailEt.setError("Incorrect email or password.");
                emailEt.requestFocus();
            }

        });
    }

    private void signUp(AppCompatActivity parent) {
        Intent i = new Intent(parent, SignUp.class);
        startActivity(i);
    }
}