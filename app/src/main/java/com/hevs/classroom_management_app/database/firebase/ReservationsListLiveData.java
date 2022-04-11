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

import java.util.ArrayList;
import java.util.List;


public class ReservationsListLiveData extends LiveData<List<ReservationWithTeacher>> {

    private static final String TAG = "ClientAccountsLiveData";

    private final DatabaseReference reference;
    private final String owner;
    private final ReservationsListLiveData.MyValueEventListener listener =
            new ReservationsListLiveData.MyValueEventListener();

    public ReservationsListLiveData(DatabaseReference ref, String owner) {
        reference = ref;
        this.owner = owner;
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

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toReservationWithTeacher(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.e(TAG, "Can't listen to query " + reference, databaseError.toException());
        }
    }

    private List<ReservationWithTeacher> toReservationWithTeacher(DataSnapshot snapshot) {
        List<ReservationWithTeacher> clientWithAccountsList = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            if (!childSnapshot.getKey().equals(owner)) {
                ReservationWithTeacher reservationWithTeacher = new ReservationWithTeacher();
                reservationWithTeacher.reservation = childSnapshot.getValue(Reservation.class);
                reservationWithTeacher.reservation.setReservationId(childSnapshot.getKey());
                reservationWithTeacher.teacher = toAccounts(childSnapshot.child("accounts"),
                        childSnapshot.getKey());
                clientWithAccountsList.add(reservationWithTeacher);
            }
        }
        return clientWithAccountsList;
    }

    private List<AccountEntity> toAccounts(DataSnapshot snapshot, String ownerId) {
        List<AccountEntity> accounts = new ArrayList<>();
        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
            AccountEntity entity = childSnapshot.getValue(AccountEntity.class);
            entity.setId(childSnapshot.getKey());
            entity.setOwner(ownerId);
            accounts.add(entity);
        }
        return accounts;
    }
}
