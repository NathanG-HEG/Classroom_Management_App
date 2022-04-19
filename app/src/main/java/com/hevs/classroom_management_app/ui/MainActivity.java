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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;


public class MainActivity extends AppCompatActivity {

    public static final String ID_TEACHER = "idTeacher";
    private FirebaseAuth mAuth;
    private TeacherRepository teacherRepository;

    @Override
    public void onBackPressed() {
        //do nothing to avoid the user to go back to activities without being logged in
    }

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
                login();
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

        //Forgot password listener
        TextView forgot = findViewById(R.id.forgotPassword);
        forgot.setOnClickListener(view -> sendNewPassword());

    }

    private void sendNewPassword() {
        EditText emailEt = findViewById(R.id.editTextTextEmailAddress);
        String email = emailEt.getText().toString();

        if (!email.equals("")) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(MainActivity.this, "A password reset link has been sent", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void checkIfUserIsLoggedIn() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String teacherId = sharedPref.getString(MainActivity.ID_TEACHER, null);
        FirebaseUser user = mAuth.getCurrentUser();
        if (teacherId != null && user != null) {
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

    private void login() {
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
        teacherRepository.getById(user.getUid()).observe(MainActivity.this, teacher -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(ID_TEACHER, teacher.getId());
            editor.commit();
            Intent i = new Intent(this, ClassroomListActivity.class);
            startActivity(i);
        });
    }

    private void signUp(AppCompatActivity parent) {
        Intent i = new Intent(parent, SignUp.class);
        startActivity(i);
    }
}