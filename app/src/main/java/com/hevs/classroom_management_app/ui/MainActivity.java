package com.hevs.classroom_management_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

public class MainActivity extends AppCompatActivity {

    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        teacherRepository = ((BaseApp)getApplication()).getTeacherRepository();

        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(MainActivity.this);
            }
        });

    }

    private void login(AppCompatActivity parent) {
        String email = findViewById(R.id.editTextTextEmailAddress).toString();
        String password = findViewById(R.id.editTextTextPassword).toString();

        LiveData <Teacher> teacherLiveData = teacherRepository.getByLogin(email, password, getApplication());
        Teacher teacher = teacherLiveData.getValue();

        if (teacher != null) {
            Intent i = new Intent(parent, ClassroomListActivity.class);
            i.putExtra("TeacherId", teacher.getId());
            startActivity(i);
        } else {
            findViewById(R.id.editTextTextEmailAddress).requestFocus();
            TextView errorMessage = findViewById(R.id.loginErrorMessage);
            errorMessage.requestFocus();
            errorMessage.setText("Incorrect email or password");
        }
    }
}