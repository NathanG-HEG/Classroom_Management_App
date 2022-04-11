package com.hevs.classroom_management_app.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.firebase.ClassroomLiveData;
import com.hevs.classroom_management_app.database.firebase.ReservationLiveData;
import com.hevs.classroom_management_app.database.firebase.ReservationsListLiveData;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepository {
    private static ReservationRepository instance;
    private final String CLASSROOMS = "classrooms";
    private final String RESERVATIONS = "reservations";

    private ReservationRepository() {
    }

    public static ReservationRepository getInstance() {
        if (instance == null) {
            synchronized (ReservationRepository.class) {
                if (instance == null) {
                    instance = new ReservationRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Reservation> getById(final String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS)
                .child(id);
        return new ReservationLiveData(ref);
    }

    public ReservationsListLiveData getReservationsByClassId(final String id) {
        Query query = FirebaseDatabase.getInstance().getReference(CLASSROOMS).child("classroomId").equalTo(id);
        return new ReservationsListLiveData(query.getRef());
    }

    public ReservationsListLiveData getReservationsByTeacherId(final String id) {
        Query query = FirebaseDatabase.getInstance().getReference(CLASSROOMS).child("teacherId").equalTo(id);
        return new ReservationsListLiveData(query.getRef());
    }

    public void insert(final Reservation reservation, OnAsyncEventListener callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS);
        String key = ref.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS)
                .child(key)
                .setValue(reservation, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Reservation reservation, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS);
        FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS)
                .child(reservation.getReservationId())
                .updateChildren(reservation.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Reservation reservation, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS);
        FirebaseDatabase.getInstance()
                .getReference(RESERVATIONS)
                .child(reservation.getReservationId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

}
