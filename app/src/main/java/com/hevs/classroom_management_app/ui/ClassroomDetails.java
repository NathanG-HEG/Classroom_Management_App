package com.hevs.classroom_management_app.ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.adapter.RecyclerAdapter;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;

import java.util.LinkedList;
import java.util.List;

public class ClassroomDetails extends AppCompatActivity {

    public static final String ID_CLASSROOM = "idClassroom";
    private ReservationRepository reservationRepository;
    private TeacherRepository teacherRepository;
    private List<ReservationWithTeacher> reservationsList;
    private RecyclerAdapter<ReservationWithTeacher> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_classroom);

        //TODO: what is that?
        RecyclerView recyclerView = findViewById(R.id.accountsRecyclerView);
        reservationRepository = ((BaseApp) getApplication()).getReservationRepository();
        teacherRepository = ((BaseApp) getApplication()).getTeacherRepository();

        long classroomId = getIntent().getExtras().getLong(ClassroomListActivity.ID_CLASSROOM);
        reservationsList = new LinkedList<>();
        reservationRepository.getReservationsByClassId(classroomId, getApplication()).observe(this, reservations -> {
            // Assign the reservation and teacher in each reservationWithTeacher POJO
            for(Reservation r : reservations){
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
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which)->{
                    alertDialog.dismiss();
                });
                String text = "Hello world!";
                alertDialog.setMessage(text);
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + reservationsList.get(position));

            }
        });
        adapter.setData(reservationsList);
        recyclerView.setAdapter(adapter);
    }
}
