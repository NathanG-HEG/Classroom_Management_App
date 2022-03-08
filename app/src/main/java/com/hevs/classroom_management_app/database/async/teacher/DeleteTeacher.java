package com.hevs.classroom_management_app.database.async.teacher;

import android.content.Context;
import android.os.AsyncTask;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class DeleteTeacher extends AsyncTask<Teacher, Void, Void> {
    private AppDatabase database;
    private OnAsyncEventListener callback;
    private Exception exception;

    public DeleteTeacher(Context context, OnAsyncEventListener callback){
        database = AppDatabase.getInstance(context);
        this.callback = callback;
    }

    @Override
    protected Void doInBackground(Teacher... teachers) {
        try {
            for (Teacher t : teachers) {
                database.teacherDao().delete(t);
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

