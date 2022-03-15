package com.hevs.classroom_management_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;

import java.sql.Date;
import java.time.LocalDateTime;

public class BookClassroom extends AppCompatActivity {

    private ReservationRepository reservationRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_a_classroom);

        reservationRepo = ReservationRepository.getInstance();

        Button bookButton = (Button) findViewById(R.id.bookNowButton);
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookAClassroom();
            }
        });


    }

    private void bookAClassroom() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        long classroomId = getIntent().getExtras().getLong(ClassroomDetails.ID_CLASSROOM);
        long teacherId = sharedPreferences.getLong(MainActivity.ID_TEACHER, -1);
        LocalDateTime startTime;
        //Reservation reservation = new Reservation
    }
}
