package com.hevs.classroom_management_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class BookClassroom extends AppCompatActivity {

    private ReservationRepository reservationRepo;
    private final String BAD_DATE_ERROR = "Enter a valid date";
    private final String BAD_TIME_ERROR = "Enter a valid time";

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
        long teacherId, classroomId;
        LocalDateTime startTime, endTime;
        int occupantsNumber;

        EditText dateEt = ((EditText) findViewById(R.id.dateInput));
        EditText startTimeEt = ((EditText) findViewById(R.id.startTimeInput));
        EditText endTimeEt = ((EditText) findViewById(R.id.endTimeInput));
        EditText participants = ((EditText) findViewById(R.id.occupantsInput));

        // Get teacherId and classroomId
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //classroomId = getIntent().getExtras().getLong(ClassroomDetails.ID_CLASSROOM);
        teacherId = sharedPreferences.getLong(MainActivity.ID_TEACHER, -1);

        // Get occupantsNumber
        occupantsNumber = Integer.parseInt(participants.getText().toString());
        // TODO: Checks if the occupants number is greater than the classroom capacity, less than 1 or null

        String date = dateEt.getText().toString();
        String startTime_s = startTimeEt.getText().toString();
        String endTime_s = endTimeEt.getText().toString();
        try {
            startTime = extractLocalDateTimeFromString(date, startTime_s);
            endTime = extractLocalDateTimeFromString(date, endTime_s);
            if (!startTime.isBefore(endTime)) throw new DateTimeException(BAD_TIME_ERROR);
        } catch (DateTimeException dte) {
            switch (dte.getMessage()) {
                case BAD_DATE_ERROR:
                    dateEt.setError(BAD_DATE_ERROR);
                    break;
                case BAD_TIME_ERROR:
                    startTimeEt.setError(BAD_TIME_ERROR);
                    endTimeEt.setError(BAD_TIME_ERROR);
                    break;
                default:
                    dateEt.setError("Unexpected error in date or time field");
            }
            return;
        }

        //TODO: Insert reservation in Room DB
        System.out.println("Done!");
    }

    private LocalDateTime extractLocalDateTimeFromString(final String date, final String time) throws DateTimeException {
        int year, month, day, hour, minute;
        // Parses the date into year, month and day and checks the validity
        String dateArray[] = date.split("/");
        if(dateArray.length!=3) throw new DateTimeException(BAD_DATE_ERROR);
        year = Integer.parseInt(dateArray[2]);
        month = Integer.parseInt(dateArray[1]);
        day = Integer.parseInt(dateArray[0]);
        try {
            LocalDateTime.of(year, month, day, 0, 0);
        } catch (DateTimeException dte) {
            throw new DateTimeException(BAD_DATE_ERROR);
        }

        // Parses the time into hour and minutes and checks the validity
        String timeArray[] = time.split(":");
        if (timeArray.length != 2) throw new DateTimeException(BAD_TIME_ERROR);
        hour = Integer.parseInt(timeArray[0]);
        minute = Integer.parseInt(timeArray[1]);
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            throw new DateTimeException(BAD_TIME_ERROR);
        }
        return LocalDateTime.of(year, month, day, hour, minute);
    }
}