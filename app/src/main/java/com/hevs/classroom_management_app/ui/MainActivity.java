package com.hevs.classroom_management_app.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static final String ID_TEACHER = "idTeacher";
    private FirebaseAuth mAuth;
    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        /*
        if the user already log in once on this phone, he doesn't have to go
        through login again. Load his preferences as well
         */
        checkIfUserIsLoggedIn();

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

    private void checkIfUserIsLoggedIn() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        long teacherId = sharedPref.getLong(MainActivity.ID_TEACHER, 0L);
        if (teacherId != 0L) {
            setTheme(sharedPref); //light or dark mode
            //skips login screen
            Intent i = new Intent(MainActivity.this, ClassroomListActivity.class);
            startActivity(i);
        }

    }

    private void setTheme(SharedPreferences sharedPref) {
        boolean isNightMode = sharedPref.getBoolean(Settings.THEME_PREFERENCE, false);
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    private void login(AppCompatActivity parent) {
        EditText emailEt = findViewById(R.id.editTextTextEmailAddress);
        EditText passwordEt = findViewById(R.id.editTextTextPassword);
        String email = emailEt.getText().toString();
        String password = passwordEt.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            setSharedPreferences(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.login_error),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setSharedPreferences(FirebaseUser user) {
        teacherRepository.getByEmail(user.getEmail()).observe(MainActivity.this, teacher -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(ID_TEACHER, teacher.getId());
        });


    }


    private void signUp(AppCompatActivity parent) {
        Intent i = new Intent(parent, SignUp.class);
        startActivity(i);
    }
}