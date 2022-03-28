package com.hevs.classroom_management_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    public static final String ID_TEACHER = "idTeacher";
    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        String email = emailEt.getText().toString();
        teacherRepository.getByEmail(email, getApplication()).observe(this, teacher -> {
            if (teacher != null) {
                // Gets the typed password
                String password = ((EditText) findViewById(R.id.editTextTextPassword)).getText().toString();
                //Gets the corresponding salt
                String salt = teacher.getSalt();
                // gets the Message digest
                String hashedAndSaltPwd = hash(password, salt);

                if (hashedAndSaltPwd.equals(teacher.getDigest())) {
                    Intent i = new Intent(parent, ClassroomListActivity.class);
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putLong(ID_TEACHER, teacher.getId());
                    editor.commit();
                    startActivity(i);
                } else {
                    //Incorrect password
                    emailEt.setError("Incorrect email or password.");
                    emailEt.requestFocus();
                }
            } else {
                // Incorrect email
                emailEt.setError("Incorrect email or password.");
                emailEt.requestFocus();
            }
        });
    }

//        teacherRepository.getByLogin(email, digest, getApplication()).observe(MainActivity.this, teacher -> {
//            if (teacher != null) {
//                Intent i = new Intent(parent, ClassroomListActivity.class);
//                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//                SharedPreferences.Editor editor = sharedPref.edit();
//                editor.putLong(ID_TEACHER, teacher.getId());
//                editor.commit();
//                startActivity(i);
//            } else {
//                emailEt.setError("Incorrect email or password.");
//                emailEt.requestFocus();
//            }
//
//        });
//    }

    private String hash(String text, String salt){
        byte[] s = salt.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(s);
        return new String(md.digest(text.getBytes(StandardCharsets.UTF_8)));
    }

    private void signUp(AppCompatActivity parent) {
        Intent i = new Intent(parent, SignUp.class);
        startActivity(i);
    }
}