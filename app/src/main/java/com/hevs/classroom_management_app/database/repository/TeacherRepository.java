package com.hevs.classroom_management_app.database.repository;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.async.teacher.CreateTeacher;
import com.hevs.classroom_management_app.database.async.teacher.DeleteTeacher;
import com.hevs.classroom_management_app.database.async.teacher.UpdateTeacher;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class TeacherRepository {
    private static TeacherRepository instance;
    private TeacherRepository(){
    }

    public static TeacherRepository getInstance(){
        if (instance == null) {
            synchronized (TeacherRepository.class) {
                if (instance == null) {
                    instance = new TeacherRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Teacher> getByLogin(final String email, final String password, Application application) {
        return ((BaseApp) application).getDatabase().teacherDao().getByLogin(email, password);
    }

    public LiveData<Teacher> getByEmail (final String email, Application application) {
        return ((BaseApp)application).getDatabase().teacherDao().getByEmail(email);
    }

    public LiveData<Teacher> getById(final long id, Context context) {
        return AppDatabase.getInstance(context).teacherDao().getById(id);
    }

    public void insert(final Teacher teacher, OnAsyncEventListener callback, Application application) {
        new CreateTeacher(application, callback).execute(teacher);
    }

    public void update(final Teacher teacher, OnAsyncEventListener callback, Application application) {
        new UpdateTeacher(application, callback).execute(teacher);
    }

    public void delete(final Teacher teacher, OnAsyncEventListener callback, Application application) {
        new DeleteTeacher(application, callback).execute(teacher);
    }
}
