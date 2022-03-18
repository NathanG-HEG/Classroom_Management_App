package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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


    public final static String ID_CLASSROOM = "classroomId";
    private List<Classroom> classrooms;
    private RecyclerAdapter<Classroom> adapter;
    private ClassroomRepository classroomRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classroom_list_activity);

        RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);
        classroomRepository = ((BaseApp) getApplication()).getClassroomRepository();

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                GridLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        classrooms = new ArrayList<>();
        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + classrooms.get(position).getName());

                Intent intent = new Intent(ClassroomListActivity.this, CreateClassroomActivity.class);
                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NO_ANIMATION |
                                Intent.FLAG_ACTIVITY_NO_HISTORY
                );
                intent.putExtra("accountId", classrooms.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + classrooms.get(position).getName());
            }
        });


        /*
        AccountListViewModel.Factory factory = new AccountListViewModel.Factory(
                getApplication(), user);
        viewModel = ViewModelProviders.of(this, factory).get(AccountListViewModel.class);
        viewModel.getOwnAccounts().observe(this, accountEntities -> {
            if (accountEntities != null) {
                classrooms = accountEntities;
                adapter.setData(classrooms);
            }
        });

         */

        classroomRepository.getAll(getApplication()).observe(ClassroomListActivity.this, classroomsList -> {
            if (classroomsList != null) {
                classrooms = classroomsList;
                adapter.setData(classrooms);
            }
        });
        recyclerView.setAdapter(adapter);
    }

}
