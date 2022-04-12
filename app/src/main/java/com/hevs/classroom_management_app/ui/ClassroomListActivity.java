package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;

import static android.content.ContentValues.TAG;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.adapter.RecyclerAdapterForGridLayout;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClassroomListActivity extends AppCompatActivity {


    public final static String ID_CLASSROOM = "idClassroom";
    private List<Classroom> classrooms;
    private RecyclerAdapterForGridLayout<Classroom> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classroom_list_activity);

        RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);
        ClassroomRepository classroomRepository = ((BaseApp) getApplication()).getClassroomRepository();

        disableBackButton();
        setGreetingsMessage();

        // Using a grid layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        classrooms = new ArrayList<>();
        adapter = new RecyclerAdapterForGridLayout<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + classrooms.get(position).getName());

                Intent intent = new Intent(ClassroomListActivity.this, ClassroomDetails.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra(ID_CLASSROOM, classrooms.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + classrooms.get(position).getName());
                Toast.makeText(getApplication().getApplicationContext(), classrooms.get(position).getName() +
                        "\nCapacity: " + classrooms.get(position).getCapacity(), Toast.LENGTH_SHORT).show();
            }
        });

        //fill the adapter with classrooms
        classroomRepository.getAll().observe(ClassroomListActivity.this, classroomsList -> {
            if (classroomsList != null) {
                classrooms = classroomsList;
                adapter.setData(classrooms);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton createButton = findViewById(R.id.createClassroomFromListButton);
        createButton.setOnClickListener(view -> {
            Intent i = new Intent(ClassroomListActivity.this, CreateClassroomActivity.class);
            startActivity(i);
        });
    }

    private void setGreetingsMessage() {
        TextView greetingsTv = findViewById(R.id.greetingsTv);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String teacherId = sharedPref.getString(MainActivity.ID_TEACHER, null);
        TeacherRepository repo = TeacherRepository.getInstance();
        repo.getById(teacherId).observe(ClassroomListActivity.this, teacher -> {
            if (teacher == null) {
                return;
            }
            // messageId => 0: default, 1: good morning, 2: good evening
            int messageId = 0;
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime endOfMorning = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 11, 0);
            LocalDateTime startOfEvening = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 18, 30);
            if (now.isBefore(endOfMorning)) {
                messageId = 1;
            }
            if (now.isAfter(startOfEvening)) {
                messageId = 2;
            }
            StringBuilder greetings = new StringBuilder();
            switch (messageId) {
                case 1:
                    greetings.append(getResources().getString(R.string.morning_greetings));
                    break;
                case 2:
                    greetings.append(getResources().getString(R.string.evening_greetings));
                    break;
                default:
                    greetings.append(getResources().getString(R.string.default_greetings));
                    break;
            }
            greetings.append(teacher.getFirstname());
            greetingsTv.setText(greetings.toString());
        });
    }

    private void disableBackButton() {
        if (getSupportActionBar() != null) {
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(ClassroomListActivity.this, Settings.class);
                startActivity(i);
                break;

            case R.id.action_logout:
                logout();
        }
        return true;
    }

    private void logout() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme).create();
        alertDialog.setTitle(R.string.logoutConfirmDialog);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.logout), (dialog, which) -> {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(MainActivity.ID_TEACHER);
            editor.remove(Settings.THEME_PREFERENCE);
            editor.commit();
            Intent i = new Intent(ClassroomListActivity.this, MainActivity.class);
            startActivity(i);
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();


    }

}
