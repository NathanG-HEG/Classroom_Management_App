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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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
                    deleteAccount();
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

    private void deleteAccount() {
        long teacherId = sharedPref.getLong(MainActivity.ID_TEACHER, 0L);
        //delete reservations related to this teacher
        ReservationRepository reservationRepository = ReservationRepository.getInstance();
        reservationRepository.getReservationsByTeacherId(teacherId, getApplication()).observe(Settings.this, reservations -> {
            for (Reservation r : reservations) {
                reservationRepository.delete(r, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        //assert that all reservations have been deleted before deleting the teacher
                        if (r.equals(reservations.get(reservations.size() - 1))) {
                            deleteTeacher(teacherId);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(Settings.this, "Internal error", Toast.LENGTH_LONG).show();
                    }
                }, getApplication());
            }
        });

    }

    private void deleteTeacher(long teacherId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        Teacher teacherToDelete = new Teacher();
        teacherToDelete.setId(teacherId);
        repo.delete(teacherToDelete, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                editor.remove(MainActivity.ID_TEACHER);
                editor.apply();
                Intent i = new Intent(Settings.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(Exception e) {
                System.out.println(e.getMessage());
                Toast.makeText(getApplication(), getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
            }
        }, getApplication());
    }
}
