package com.hevs.classroom_management_app.ui;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.adapter.RecyclerAdapter;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;

import java.util.LinkedList;
import java.util.List;

public class ClassroomDetails extends AppCompatActivity {

    public static final String ID_CLASSROOM = "idClassroom";
    private ReservationRepository reservationRepository;
    private TeacherRepository teacherRepository;
    private ClassroomRepository classroomRepository;
    private List<ReservationWithTeacher> reservationsList;
    private RecyclerAdapter<ReservationWithTeacher> adapter;
    private long teacherId;
    private long classroomId;

    //TODO:
    // Fix display bug
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations_list_view);

        RecyclerView recyclerView = findViewById(R.id.reservations_w_teacher);
        reservationRepository = ((BaseApp) getApplication()).getReservationRepository();
        teacherRepository = ((BaseApp) getApplication()).getTeacherRepository();
        classroomRepository = ((BaseApp) getApplication()).getClassroomRepository();
        classroomId = getIntent().getExtras().getLong(ClassroomListActivity.ID_CLASSROOM);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        teacherId = sharedPref.getLong(MainActivity.ID_TEACHER, 0L);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TextView classroomNameTv = findViewById(R.id.classroomNameDetailsTv);
        classroomRepository.getById(classroomId, getApplication()).observe(this, classroom -> {
            classroomNameTv.setText(classroom.getName());
        });


        reservationsList = new LinkedList<>();
        reservationRepository.getReservationsByClassId(classroomId, getApplication()).observe(this, reservations -> {
            // Assign the reservation and teacher in each reservationWithTeacher POJO
            for (Reservation r : reservations) {
                ReservationWithTeacher rwt = new ReservationWithTeacher();
                rwt.reservation = r;
                teacherRepository.getById(r.getTeacherId(), getApplication()).observe(this, teacher -> {
                    rwt.teacher = teacher;
                });
                reservationsList.add(rwt);
            }
        });

        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + reservationsList.get(position));
                final AlertDialog alertDialog = new AlertDialog.Builder(ClassroomDetails.this).create();
                alertDialog.setTitle(R.string.reservation_text);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> {
                    alertDialog.dismiss();
                });
                String text = "Hello world!";
                alertDialog.setMessage(text);
                alertDialog.show();
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + reservationsList.get(position));
                final AlertDialog alertDialog = new AlertDialog.Builder(ClassroomDetails.this).create();
                alertDialog.setTitle(R.string.reservation_text);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
                    alertDialog.dismiss();
                });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.delete_reservation_confirm),
                        (dialog, which) -> {
                            if (reservationsList.get(position).teacher.getId() != teacherId) {
                                System.out.println(getString(R.string.deny_permission));
                                Toast toast = Toast.makeText(ClassroomDetails.this,
                                        getString(R.string.deny_permission), Toast.LENGTH_LONG);
                                toast.show();
                                return;
                            }
                            reservationRepository.delete(reservationsList.get(position).reservation, new OnAsyncEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onSuccess() {
                                    adapter.notifyDataSetChanged();
                                    Toast toast = Toast.makeText(ClassroomDetails.this,
                                            getString(R.string.deleted_successfully), Toast.LENGTH_SHORT);
                                    toast.show();
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    Toast toast = Toast.makeText(ClassroomDetails.this,
                                            getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            }, getApplication());
                        });
                String text = "Hello world!";
                alertDialog.setMessage(text);
                alertDialog.show();
            }
        });

        adapter.setData(reservationsList);
        recyclerView.setAdapter(adapter);
        editBtnInitialize();
        bookBtnInitialize();
    }

    private void editBtnInitialize(){
        FloatingActionButton editBtn = ((FloatingActionButton) findViewById(R.id.EditClassroomBtn));
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClassroomDetails.this, EditClassroom.class);
                i.putExtra(ClassroomDetails.ID_CLASSROOM, classroomId);
                startActivity(i);
            }
        });
    }

    private void bookBtnInitialize(){
        Button bookBtn = ((Button) findViewById(R.id.book_now_btn));
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ClassroomDetails.this, BookClassroom.class);
                i.putExtra(ClassroomDetails.ID_CLASSROOM, classroomId);
                startActivity(i);
            }
        });
    }
}
