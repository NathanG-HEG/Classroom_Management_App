package com.hevs.classroom_management_app.database.firebase;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hevs.classroom_management_app.database.entity.Reservation;

public class ReservationLiveData extends LiveData<Reservation> {

    private static final String TAG = "ReservationLiveData: ";

    private final DatabaseReference reference;
    private final ReservationLiveData.MyValueEventListener listener =
            new ReservationLiveData.MyValueEventListener();

    public ReservationLiveData(DatabaseReference ref){
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
            Reservation reservation = dataSnapshot.getValue(Reservation.class);
            if (reservation != null) {
                reservation.setReservationId(dataSnapshot.getKey());
                setValue(reservation);
            }
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError){
            // Manage error
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

}
