package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hevs.classroom_management_app.R;

import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;


public class Settings extends AppCompatActivity {

    public static final String THEME_PREFERENCE = "theme_preference";
    public static final String US_DATE_FORMAT = "usDateFormat";
    private SharedPreferences sharedPref;
    private TeacherRepository repo;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        repo = TeacherRepository.getInstance();

        //toggle night theme button
        FloatingActionButton nightMode = findViewById(R.id.night_mode_button);
        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleTheme();
            }
        });

        //about button
        FloatingActionButton about = findViewById(R.id.about_button);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Settings.this, About.class);
                startActivity(i);
            }
        });

        //Delete btn
        FloatingActionButton deleteAccount = findViewById(R.id.delte_account_btn);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Settings.this, R.style.MyAlertDialogTheme).create();
                alertDialog.setTitle(getString(R.string.delete_account_confirm));
                alertDialog.setMessage(getString(R.string.delete_account_message));
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
                    alertDialog.dismiss();
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm), (dialog, which) -> {
                    String teacherId = sharedPref.getString(MainActivity.ID_TEACHER, null);
                    deleteTeacher(teacherId);
                });
                alertDialog.show();
            }
        });

        //Change password btn
        FloatingActionButton changePwd = findViewById(R.id.change_pwd_btn);
        changePwd.setOnClickListener(view -> {
            Intent i = new Intent(Settings.this, ChangePwd.class);
            startActivity(i);
        });

        //Send verification email
        FloatingActionButton verifyEmail = findViewById(R.id.send_verification_email_button);
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    Toast.makeText(Settings.this, R.string.already_verified, Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.getCurrentUser().sendEmailVerification();
                Toast.makeText(Settings.this, getResources().getString(R.string.verification_email_sent) +
                        " to: " + mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

            }
        });

        // Use US DateTime format
        Switch usDateTime = findViewById(R.id.us_date_format_switch);
        usDateTime.setChecked(sharedPref.getBoolean(US_DATE_FORMAT, false));
        usDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchDateFormat();
            }
        });
    }

    private void toggleTheme() {
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

    private void switchDateFormat() {
        boolean usDateFormat = sharedPref.getBoolean(US_DATE_FORMAT, false);
        sharedPref.edit().putBoolean(US_DATE_FORMAT, !usDateFormat).commit();
        System.out.println(sharedPref.getBoolean(US_DATE_FORMAT, false));
    }

    private void deleteTeacher(String teacherId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Teacher teacherToDelete = new Teacher();
        teacherToDelete.setId(teacherId);
        repo.delete(teacherToDelete, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                editor.remove(MainActivity.ID_TEACHER);
                editor.commit();
                mAuth.getCurrentUser().delete();
                Intent i = new Intent(Settings.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e.getMessage());
                Toast.makeText(getApplication(), getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
            }
        });
    }
}
