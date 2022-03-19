package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.lifecycle.ViewModelProviders;
import static android.content.ContentValues.TAG;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.adapter.RecyclerAdapter;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class ClassroomListActivity extends AppCompatActivity {


    public final static String ID_CLASSROOM = "idClassroom";
    private List<Classroom> classrooms;
    private RecyclerAdapter<Classroom> adapter;
    private ClassroomRepository classroomRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classroom_list_activity);

        RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);
        classroomRepository = ((BaseApp) getApplication()).getClassroomRepository();

        disableBackButton();

        // using a grid layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        classrooms = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + classrooms.get(position).getName());

                Intent intent = new Intent(ClassroomListActivity.this, EditClassroom.class);
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
                        "\nCapacity: " + classrooms.get(position).getCapacity(), Toast.LENGTH_LONG).show();

            }
        });

        //fill the adapter with classrooms
        classroomRepository.getAll(getApplication()).observe(ClassroomListActivity.this, classroomsList -> {
            if (classroomsList != null) {
                classrooms = classroomsList;
                adapter.setData(classrooms);
            }
        });
        recyclerView.setAdapter(adapter);

        FloatingActionButton createButton = findViewById(R.id.createClassroomFromListButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClassroomListActivity.this, CreateClassroomActivity.class);
                startActivity(i);
            }
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

            case R.id.action_about:

                break;

            case R.id.action_logout:
                logout();
        }

        return true;
    }

    private void logout() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(MainActivity.ID_TEACHER);
        editor.commit();
        Intent i = new Intent(ClassroomListActivity.this, MainActivity.class);
        startActivity(i);
    }

}
