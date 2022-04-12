package com.hevs.classroom_management_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class BookClassroom extends AppCompatActivity {

    private ReservationRepository reservationRepo;
    private ClassroomRepository classroomRepository;
    private final String BAD_DATE_ERROR = "Enter a valid date";
    private final String BAD_TIME_ERROR = "Enter a valid time";
    private String teacherId, classroomId;
    private EditText dateEt, startTimeEt, endTimeEt, participants, reservationText;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_a_classroom);

        reservationRepo = ReservationRepository.getInstance();
        classroomRepository = ClassroomRepository.getInstance();

        dateEt = ((EditText) findViewById(R.id.dateInput));
        startTimeEt = ((EditText) findViewById(R.id.startTimeInput));
        endTimeEt = ((EditText) findViewById(R.id.endTimeInput));
        participants = ((EditText) findViewById(R.id.occupantsInput));
        reservationText = ((EditText) findViewById(R.id.reservation_text_et));

        // Get teacherId and classroomId
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        classroomId = getIntent().getExtras().getString(ClassroomDetails.ID_CLASSROOM);
        teacherId = sharedPreferences.getString(MainActivity.ID_TEACHER, null);

        // Sets correct date format
        if (sharedPreferences.getBoolean(Settings.US_DATE_FORMAT, false)) {
            dateEt.setHint(R.string.date_hint_us);
        } else {
            dateEt.setHint(R.string.date_hint);
        }

        TextView classroomNameTv = findViewById(R.id.classroomNameTv);
        classroomRepository.getById(classroomId).observe(BookClassroom.this, classroom -> classroomNameTv.setText(classroom.getName()));

        Button bookButton = (Button) findViewById(R.id.bookNowButton);
        bookButton.setOnClickListener(view -> bookAClassroom());
    }

    private void bookAClassroom() {
        LocalDateTime startTime, endTime;
        int occupantsNumber;

        // Get occupantsNumber
        try {
            occupantsNumber = Integer.parseInt(participants.getText().toString());
        } catch (NumberFormatException nfe) {
            participants.setError("Min participants is 1");
            return;
        }
        // Checks if the occupants number is greater than the classroom capacity, less than 1 or null
        if (occupantsNumber < 1) {
            participants.setError("Min participants is 1");
            return;
        }

        classroomRepository.getById(classroomId).observe(BookClassroom.this, classroom -> {
            if (occupantsNumber > classroom.getCapacity()) {
                participants.setError("Max participants is " + classroom.getCapacity());
            }
        });

        String date = dateEt.getText().toString();
        String startTime_s = startTimeEt.getText().toString();
        String endTime_s = endTimeEt.getText().toString();

        try {
            boolean usFormat = sharedPreferences.getBoolean(Settings.US_DATE_FORMAT, false);
            startTime = extractLocalDateTimeFromString(date, startTime_s, usFormat);
            endTime = extractLocalDateTimeFromString(date, endTime_s, usFormat);
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

        //Get Reservation text
        String reservationText = this.reservationText.getText().toString();

        // Inserts in DB
        Reservation newReservation = new Reservation(classroomId, startTime, endTime, teacherId, occupantsNumber, reservationText);
        reservationRepo.insert(newReservation, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(BookClassroom.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT);
                toast.show();
                NavUtils.navigateUpFromSameTask(BookClassroom.this);
            }

            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(BookClassroom.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    private LocalDateTime extractLocalDateTimeFromString(final String date, final String time, boolean usFormat) throws DateTimeException {
        int year, month, day, hour, minute;
        // Parses the date into year, month and day and checks the validity
        String dateArray[] = date.split("/");
        if (dateArray.length != 3) throw new DateTimeException(BAD_DATE_ERROR);
        year = Integer.parseInt(dateArray[2]);
        month = Integer.parseInt(dateArray[usFormat ? 0 : 1]);
        day = Integer.parseInt(dateArray[usFormat ? 1 : 0]);
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
