package com.hevs.classroom_management_app.database.async.reservation;

import android.content.Context;
import android.os.AsyncTask;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class CreateReservation extends AsyncTask<Reservation, Void, Void> {
    private AppDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public CreateReservation(Context context, OnAsyncEventListener callback) {
        database = AppDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Reservation... reservations) {
        try {
            for (Reservation r : reservations) {
                database.reservationDao().insert(r);
            }
        } catch (Exception e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (callback != null) {
            if (exception == null) {
                callback.onSuccess();
            } else {
                callback.onFailure(exception);
            }
        }
    }
}
