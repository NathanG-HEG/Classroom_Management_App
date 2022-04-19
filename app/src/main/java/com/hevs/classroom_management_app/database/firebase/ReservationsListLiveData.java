package com.hevs.classroom_management_app.database.firebase;

import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hevs.classroom_management_app.database.entity.Reservation;

import java.util.LinkedList;
import java.util.List;


public class ReservationsListLiveData extends LiveData<List<Reservation>> {

    private static final String TAG = "ReservationLiveData";

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

    private List<Reservation> toReservations(DataSnapshot snapshot) {
        List<Reservation> reservations = new LinkedList<>();
        for (DataSnapshot childSnapShot : snapshot.getChildren()) {
            Reservation reservation = childSnapShot.getValue(Reservation.class);
            if (reservation != null) {
                reservation.setReservationId(childSnapShot.getKey());
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toReservations(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }
}
