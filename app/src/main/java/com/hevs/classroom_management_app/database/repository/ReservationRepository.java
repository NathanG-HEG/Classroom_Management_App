package com.hevs.classroom_management_app.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.async.reservation.CreateReservation;
import com.hevs.classroom_management_app.database.async.reservation.DeleteReservation;
import com.hevs.classroom_management_app.database.async.reservation.UpdateReservation;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

import java.util.List;

public class ReservationRepository {
    private static ReservationRepository instance;
    private ReservationRepository(){}

    public static ReservationRepository getInstance(){
        if (instance == null) {
            synchronized (ReservationRepository.class) {
                if (instance == null) {
                    instance = new ReservationRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<List<Reservation>> getReservationsByClassId(final long id, Context context) {
        return AppDatabase.getInstance(context).reservationDao().getReservationsByClassId(id);
    }

    public void insert(final Reservation reservation, OnAsyncEventListener callback, Application application) {
        new CreateReservation(application, callback).execute(reservation);
    }

    public void update(final Reservation reservation, OnAsyncEventListener callback, Application application) {
        new UpdateReservation(application, callback).execute(reservation);
    }

    public void delete(final Reservation reservation, OnAsyncEventListener callback, Application application) {
        new DeleteReservation(application, callback).execute(reservation);
    }
}
