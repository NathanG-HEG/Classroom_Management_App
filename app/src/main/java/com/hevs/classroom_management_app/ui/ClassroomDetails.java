package com.hevs.classroom_management_app.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.adapter.RecyclerAdapter;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.util.RecyclerViewItemClickListener;
import com.hevs.classroom_management_app.viewModel.ReservationListViewModel;

import java.time.LocalDateTime;
import java.util.List;

public class ClassroomDetails extends AppCompatActivity {

    public static final String ID_CLASSROOM = "idClassroom";
    public static final String START_TIME = "startTime";
    public static final String ID_RESERVATION = "idReservation";
    private ClassroomRepository classroomRepository;
    private List<Reservation> reservationsList;
    private RecyclerAdapter<Reservation> adapter;
    private String classroomId;
    private ReservationListViewModel reservationListViewModel;
    private RecyclerView recyclerView;
        private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reservations_list_view);

        recyclerView = findViewById(R.id.reservations_w_teacher);
        classroomRepository = ((BaseApp) getApplication()).getClassroomRepository();
        classroomId = getIntent().getExtras().getString(ClassroomListActivity.ID_CLASSROOM);
        mAuth = FirebaseAuth.getInstance();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        TextView classroomNameTv = findViewById(R.id.classroomNameDetailsTv);
        classroomRepository.getById(classroomId).observe(this, classroom -> classroomNameTv.setText(classroom.getName()));

        ReservationListViewModel.Factory factory = new ReservationListViewModel.Factory(getApplication(), classroomId);
        reservationListViewModel = ViewModelProviders.of(this, factory).get(ReservationListViewModel.class);
        reservationListViewModel.getReservations().observe(this, teacherReservations -> {
            if (teacherReservations != null) {
                reservationsList = teacherReservations;
                adapter.setData(reservationsList);
            }
        });

        adapter = new RecyclerAdapter<>(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Log.d(TAG, "clicked position:" + position);
                Log.d(TAG, "clicked on: " + reservationsList.get(position));
                final AlertDialog alertDialog = new AlertDialog.Builder(ClassroomDetails.this, R.style.MyAlertDialogTheme).create();
                alertDialog.setTitle(R.string.reservation_text);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> alertDialog.dismiss());
                String text = reservationsList.get(position).getReservationText();
                alertDialog.setMessage(text);
                alertDialog.show();
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Log.d(TAG, "longClicked position:" + position);
                Log.d(TAG, "longClicked on: " + reservationsList.get(position));
                final AlertDialog alertDialog = new AlertDialog.Builder(ClassroomDetails.this, R.style.MyAlertDialogTheme).create();
                alertDialog.setTitle(R.string.reservation_text);
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> alertDialog.dismiss());
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.edit_reservation), (dialog, which) -> {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Intent i = new Intent(ClassroomDetails.this, EditReservation.class);
                        i.putExtra(ClassroomListActivity.ID_CLASSROOM, classroomId);
                        i.putExtra(ID_RESERVATION, reservationsList.get(position).getReservationId());
                        LocalDateTime startTime = reservationsList.get(position).getStartTime();
                        i.putExtra(START_TIME, startTime.toString());
                        startActivity(i);
                    }else{
                        final AlertDialog alertDialog2 = new AlertDialog.Builder(ClassroomDetails.this, R.style.MyAlertDialogTheme).create();
                        alertDialog2.setTitle("Unauthorized");
                        alertDialog2.setMessage("Your e-mail address must be verified to edit a reservation.");
                        alertDialog2.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog2, which2) -> alertDialog2.dismiss());
                        alertDialog2.show();
                    }
                });

                String text = reservationsList.get(position).getReservationText();
                alertDialog.setMessage(text);
                alertDialog.show();
            }
        });

        recyclerView.setAdapter(adapter);
        editBtnInitialize();
        bookBtnInitialize();
    }

    private void editBtnInitialize() {
        FloatingActionButton editBtn = ((FloatingActionButton) findViewById(R.id.EditClassroomBtn));
        editBtn.setOnClickListener(view -> {
                    if (mAuth.getCurrentUser().isEmailVerified()) {
                        Intent i = new Intent(ClassroomDetails.this, EditClassroom.class);
                        i.putExtra(ClassroomDetails.ID_CLASSROOM, classroomId);
                        startActivity(i);
                    } else {
                        final AlertDialog alertDialog = new AlertDialog.Builder(ClassroomDetails.this, R.style.MyAlertDialogTheme).create();
                        alertDialog.setTitle("Unauthorized");
                        alertDialog.setMessage("Your e-mail address must be verified to edit a classroom");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> alertDialog.dismiss());
                        alertDialog.show();
                    }
                }
        );
    }

    private void bookBtnInitialize() {
        Button bookBtn = ((Button) findViewById(R.id.book_now_btn));
        bookBtn.setOnClickListener(view -> {
            if (mAuth.getCurrentUser().isEmailVerified()) {
                Intent i = new Intent(ClassroomDetails.this, BookClassroom.class);
                i.putExtra(ClassroomDetails.ID_CLASSROOM, classroomId);
                startActivity(i);
            } else {
                final AlertDialog alertDialog = new AlertDialog.Builder(ClassroomDetails.this, R.style.MyAlertDialogTheme).create();
                alertDialog.setTitle("Unauthorized");
                alertDialog.setMessage("Your e-mail address must be verified to book a classroom");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> alertDialog.dismiss());
                alertDialog.show();
            }
        });
    }
}
