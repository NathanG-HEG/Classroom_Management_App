package com.hevs.classroom_management_app.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.entity.Teacher;

public class TeacherRepository {
    private static TeacherRepository instance;
    private TeacherRepository(){
    }

    public TeacherRepository getInstance(){
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
        return AppDatabase.getInstance(context).clientDao().getByEmail(email);
    }

    public LiveData<List<ClientWithAccounts>> getOtherClientsWithAccounts(final String owner,
                                                                          Application application) {
        return ((BaseApp) application).getDatabase().clientDao().getOtherClientsWithAccounts(owner);
    }

    public void insert(final ClientEntity client, OnAsyncEventListener callback,
                       Application application) {
        new CreateClient(application, callback).execute(client);
    }

    public void update(final ClientEntity client, OnAsyncEventListener callback,
                       Application application) {
        new UpdateClient(application, callback).execute(client);
    }

    public void delete(final ClientEntity client, OnAsyncEventListener callback,
                       Application application) {
        new DeleteClient(application, callback).execute(client);
    }
}
