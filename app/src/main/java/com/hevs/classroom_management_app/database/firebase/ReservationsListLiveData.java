package com.hevs.classroom_management_app.database.firebase;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class ReservationsListLiveData extends LiveData<List<ReservationWithTeacher>> {

    private static final String TAG = "ClientAccountsLiveData";

    private final DatabaseReference reference;
    private final ReservationsListLiveData.MyValueEventListener listener =
            new ReservationsListLiveData.MyValueEventListener();

    public ReservationsListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    @Override
    protected void onActive() {
        Log.d(TAG, "onActive");
        reference.addValueEventListener(listener);
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive");
    }

    private List<ReservationWithTeacher> toReservationsWithTeacher(DataSnapshot snapshot) {
        List<ReservationWithTeacher> reservations = new LinkedList<>();
        TeacherRepository teacherRepository = TeacherRepository.getInstance();
        for (DataSnapshot childSnapShot : snapshot.getChildren()) {
            ReservationWithTeacher reservationWithTeacher = new ReservationWithTeacher();
            reservationWithTeacher.reservation = childSnapShot.getValue(Reservation.class);
            reservationWithTeacher.reservation.setReservationId(childSnapShot.getKey());
            reservationWithTeacher.teacher = teacherRepository.getById(reservationWithTeacher.reservation.getTeacherId()).getValue();
        }
        return reservations;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toReservationsWithTeacher(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
