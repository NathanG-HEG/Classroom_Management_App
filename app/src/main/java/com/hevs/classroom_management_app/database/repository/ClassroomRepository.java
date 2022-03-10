package com.hevs.classroom_management_app.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.async.classroom.CreateClassroom;
import com.hevs.classroom_management_app.database.async.classroom.DeleteClassroom;
import com.hevs.classroom_management_app.database.async.classroom.UpdateClassroom;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class ClassroomRepository {
    private static ClassroomRepository instance;
    private ClassroomRepository(){}

    public static ClassroomRepository getInstance(){
        if (instance == null) {
            synchronized (ClassroomRepository.class) {
                if (instance == null) {
                    instance = new ClassroomRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Classroom> getById(final long id, Context context) {
        return AppDatabase.getInstance(context).classroomDao().getById(id);
    }

    public LiveData<Classroom> getByName(final String name, Context context) {
        return AppDatabase.getInstance(context).classroomDao().getByName(name);
    }

    public void insert(final Classroom classroom, OnAsyncEventListener callback, Application application) {
        new CreateClassroom(application, callback).execute(classroom);
    }

    public void update(final Classroom classroom, OnAsyncEventListener callback, Application application) {
        new UpdateClassroom(application, callback).execute(classroom);
    }

    public void delete(final Classroom classroom, OnAsyncEventListener callback, Application application) {
        new DeleteClassroom(application, callback).execute(classroom);
    }
}
