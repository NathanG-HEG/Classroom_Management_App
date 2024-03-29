package com.hevs.classroom_management_app.database;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.entity.Teacher;

import java.sql.Date;
import java.time.LocalDateTime;

public class DatabaseInitializer {

    public static void populateDatabase(final AppDatabase db) {
        Log.i(TAG, "Inserting demo data.");
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    private static void addTeacher(final AppDatabase db, final String email, final String firstName,
                                  final String lastName, final String password) {
        Teacher teacher = new Teacher(lastName, firstName, email, password);
        db.teacherDao().insert(teacher);
    }

    private static void addClassroom(final AppDatabase db, final String name, final int capacity) {
        Classroom classroom = new Classroom(name, capacity);
        db.classroomDao().insert(classroom);
    }

    private static void addReservation(final AppDatabase db, final long classroomId, final LocalDateTime startTime,
                                       final LocalDateTime endTime, final long teacherId, int occupantsNumber, String reservationText) {
        Reservation reservation = new Reservation(classroomId, startTime, endTime, teacherId, occupantsNumber, reservationText);
        db.reservationDao().insert(reservation);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.teacherDao().deleteAll();
        db.classroomDao().deleteAll();
        db.reservationDao().deleteAll();
        /*
        Insert data sample below
         */
        addTeacher(db, "nathan@mail.ch", "Nathan", "Gaillard", "123");
        addTeacher(db, "benjamin@mail.ch", "Benjamin", "Biollaz", "123");
        addClassroom(db, "SUM", 30);
        for(int i = 0; i<12; i++) {
            addReservation(db, 1, LocalDateTime.of(2022, 3, 10, i, 30),
                    LocalDateTime.of(2022, 3, 10, i, 45),
                    i%2+1, 25, "Sample"+i);
        }
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase database;

        PopulateDbAsync(AppDatabase db) {
            database = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(database);
            return null;
        }
    }
}
