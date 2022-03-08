package com.hevs.classroom_management_app.database.async.classroom;

import android.content.Context;
import android.os.AsyncTask;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class UpdateClassroom extends AsyncTask<Classroom, Void, Void> {
    private AppDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public UpdateClassroom(Context context, OnAsyncEventListener callback) {
        database = AppDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Classroom... classrooms) {
        try {
            for (Classroom c : classrooms) {
                database.classroomDao().update(c);
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
