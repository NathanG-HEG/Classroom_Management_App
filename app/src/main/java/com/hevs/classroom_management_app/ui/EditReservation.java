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
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.viewModel.ReservationViewModel;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public class EditReservation extends AppCompatActivity {

    private final String BAD_DATE_ERROR = "Enter a valid date";
    private final String BAD_TIME_ERROR = "Enter a valid time";
    private ClassroomRepository classroomRepository;
    private long teacherId, classroomId;
    private EditText dateEt, startTimeEt, endTimeEt, participants, reservationText;
    private SharedPreferences sharedPreferences;
    private LocalDateTime oldStartTime;
    private LocalDateTime oldEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reservation);

        classroomRepository = ((BaseApp) getApplication()).getClassroomRepository();

        dateEt = findViewById(R.id.dateInput_edit);
        startTimeEt = findViewById(R.id.startTimeInput_edit);
        endTimeEt = findViewById(R.id.endTimeInput_edit);
        participants = findViewById(R.id.occupantsInput_edit);
        reservationText = findViewById(R.id.reservation_text_et_edit);

        // Get teacherId and classroomId
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        classroomId = getIntent().getExtras().getLong(ClassroomDetails.ID_CLASSROOM);
        teacherId = sharedPreferences.getLong(MainActivity.ID_TEACHER, -1);

        fillFields();

        // Sets correct date format
        if (sharedPreferences.getBoolean(Settings.US_DATE_FORMAT, false)) {
            dateEt.setHint(R.string.date_hint_us);
        } else {
            dateEt.setHint(R.string.date_hint);
        }

        Button bookButton = findViewById(R.id.bookNowButton_edit);
        bookButton.setOnClickListener(view -> updateReservation());

        FloatingActionButton deleteButton = findViewById(R.id.delete_reservation_edit);
        deleteButton.setOnClickListener(view -> deleteReservation());
    }

    private void fillFields() {
        //set classroom name
        TextView classroomNameTv = findViewById(R.id.classroomNameTv_edit);
        classroomRepository.getById(classroomId, getApplication()).observe(EditReservation.this, classroom -> {
            classroomNameTv.setText(classroom.getName());
        });

        System.out.println(classroomNameTv.getText().toString());

        String startTimeString = getIntent().getStringExtra(ClassroomDetails.START_TIME);
        oldStartTime = LocalDateTime.parse(startTimeString);

        ReservationViewModel.Factory factory = new ReservationViewModel.Factory(getApplication(), classroomId, oldStartTime);
        ReservationViewModel reservationViewModel = ViewModelProviders.of(this, factory).get(ReservationViewModel.class);
        reservationViewModel.getReservation().observe(this, reservation -> {
            if (reservation != null) {
                dateEt.setText(formatDate(reservation.getStartTime()));
                startTimeEt.setText(formatTime(reservation.getStartTime()));
                endTimeEt.setText(formatTime(reservation.getEndTime()));
                participants.setText("" + reservation.getOccupantsNumber());
                reservationText.setText(reservation.getReservationText());
                oldEndTime = reservation.getEndTime();
            }
        });
    }

    private String formatDate(LocalDateTime startTime) {
        StringBuilder sb = new StringBuilder();
        sb.append(startTime.getDayOfMonth()).append("/").append(startTime.getMonthValue())
                .append("/").append(startTime.getYear());
        return sb.toString();
    }

    private String formatTime(LocalDateTime time) {
        StringBuilder sb = new StringBuilder();
        sb.append(time.getHour()).append(":");
        if (time.getMinute() == 0)
            return sb.append(time.getMinute()).append(0).toString();
        return sb.append(time.getMinute()).toString();

    }

    private void updateReservation() {
        LocalDateTime startTime, endTime;
        int occupantsNumber;

        // Get occupantsNumber
        occupantsNumber = Integer.parseInt(participants.getText().toString());
        // Checks if the occupants number is greater than the classroom capacity, less than 1 or null
        classroomRepository.getById(classroomId, getApplication()).observe(EditReservation.this, classroom -> {
            if (occupantsNumber > classroom.getCapacity()) {
                participants.setError("Max participants is " + classroom.getCapacity());
            }
        });
        if (occupantsNumber < 1) {
            participants.setError("Min participants is 1");
            return;
        }

        //extracting time
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

        // make changes in db
        if (!startTime.equals(oldStartTime) || !endTime.equals(oldEndTime)) {
            deleteThenCreate(startTime, endTime, occupantsNumber); //primary key was changed, a new record must be created
        } else {
            updateReservation(startTime, endTime, occupantsNumber);
        }
    }

    private void deleteThenCreate(LocalDateTime startTime, LocalDateTime endTime, int occupantsNumber) {
        //used to delete reservation
        Reservation copyOfReservation = copyReservation();

        //delete reservation
        ReservationViewModel.Factory factory = new ReservationViewModel.Factory(getApplication(), classroomId, oldStartTime);
        ReservationViewModel reservationViewModel = ViewModelProviders.of(this, factory).get(ReservationViewModel.class);
        reservationViewModel.deleteReservation(copyOfReservation, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Reservation newReservation = new Reservation(classroomId, startTime, endTime, teacherId, occupantsNumber, reservationText.getText().toString());
                reservationViewModel.createReservation(newReservation, new OnAsyncEventListener() {
                    @Override
                    public void onSuccess() {
                        Toast toast = Toast.makeText(EditReservation.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT);
                        toast.show();
                        NavUtils.navigateUpFromSameTask(EditReservation.this);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast toast = Toast.makeText(EditReservation.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(EditReservation.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    private void updateReservation(LocalDateTime startTime, LocalDateTime endTime, int occupantsNumber) {
        Reservation newReservation = new Reservation(classroomId, startTime, endTime, teacherId, occupantsNumber, reservationText.getText().toString());
        ReservationViewModel.Factory factory = new ReservationViewModel.Factory(getApplication(), classroomId, oldStartTime);
        ReservationViewModel reservationViewModel = ViewModelProviders.of(this, factory).get(ReservationViewModel.class);
        reservationViewModel.updateReservation(newReservation, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(EditReservation.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT);
                toast.show();
                NavUtils.navigateUpFromSameTask(EditReservation.this);
            }

            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(EditReservation.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void deleteReservation() {
        //used to delete reservation
        Reservation copyOfReservation = copyReservation();

        //delete reservation
        ReservationViewModel.Factory factory = new ReservationViewModel.Factory(getApplication(), classroomId, oldStartTime);
        ReservationViewModel reservationViewModel = ViewModelProviders.of(this, factory).get(ReservationViewModel.class);
        reservationViewModel.deleteReservation(copyOfReservation, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(EditReservation.this, getString(R.string.delete_reservation_confirm), Toast.LENGTH_SHORT);
                toast.show();
                NavUtils.navigateUpFromSameTask(EditReservation.this);
            }

            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(EditReservation.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private Reservation copyReservation() {
        Reservation copyOfReservation = new Reservation(classroomId, oldStartTime, null, teacherId, 0, null);
        ReservationViewModel.Factory factory1 = new ReservationViewModel.Factory(getApplication(), classroomId, oldStartTime);
        ReservationViewModel reservationViewModel1 = ViewModelProviders.of(this, factory1).get(ReservationViewModel.class);
        reservationViewModel1.getReservation().observe(this, reservation -> {
            if (reservation != null) {
                copyOfReservation.setTeacherId(reservation.getTeacherId());
                copyOfReservation.setEndTime(reservation.getEndTime());
                copyOfReservation.setReservationText(reservation.getReservationText());
            }
        });
        return copyOfReservation;
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
