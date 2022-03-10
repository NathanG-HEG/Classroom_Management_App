package com.hevs.classroom_management_app.database;

import static android.content.ContentValues.TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.entity.Teacher;

import java.sql.Date;

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

    private static void addReservation(final AppDatabase db, final long classroomId, final Date startTime,
                                       final Date endTime, final long teacherId, int occupantsNumber) {
        Reservation reservation = new Reservation(classroomId, startTime, endTime, teacherId, occupantsNumber);
        db.reservationDao().insert(reservation);
    }

    private static void populateWithTestData(AppDatabase db) {
        db.teacherDao().deleteAll();
        db.classroomDao().deleteAll();
        db.reservationDao().deleteAll();
        /*
        Insert data sample below
         */
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