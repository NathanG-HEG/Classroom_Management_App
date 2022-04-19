package com.hevs.classroom_management_app.database.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.firebase.ReservationLiveData;
import com.hevs.classroom_management_app.database.firebase.ReservationsListLiveData;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

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

    public LiveData<Reservation> getById(final String resId, final String classroomId) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(classroomId)
                .child(RESERVATIONS)
                .child(resId);
        return new ReservationLiveData(ref);
    }

    public ReservationsListLiveData getReservationsByClassId(final String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(id)
                .child(RESERVATIONS);
        return new ReservationsListLiveData(ref);
    }

    public void insert(final Reservation reservation, OnAsyncEventListener callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(reservation.getClassroomId())
                .child(RESERVATIONS);
        String key = ref.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(reservation.getClassroomId())
                .child(RESERVATIONS)
                .child(key)
                .setValue(reservation.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Reservation reservation, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(reservation.getClassroomId())
                .child(RESERVATIONS)
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
                .getReference(CLASSROOMS)
                .child(reservation.getClassroomId())
                .child(RESERVATIONS)
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
