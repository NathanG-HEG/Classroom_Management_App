package com.hevs.classroom_management_app.database.repository;


import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.firebase.TeacherLiveData;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class TeacherRepository {
    private static TeacherRepository instance;
    private final String TEACHERS = "teachers";

    private TeacherRepository() {
    }

    public static TeacherRepository getInstance() {
        if (instance == null) {
            synchronized (TeacherRepository.class) {
                if (instance == null) {
                    instance = new TeacherRepository();
                }
            }
        }
        return instance;
    }

    public LiveData<Teacher> getById(final String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(TEACHERS)
                .child(id);
        return new TeacherLiveData(ref);
    }

    public LiveData<Teacher> getByEmail(final String email) {
        Query query = FirebaseDatabase.getInstance().getReference(TEACHERS).orderByChild("email").equalTo(email);
        return new TeacherLiveData(query.getRef());
    }

    public void insert(final Teacher teacher, final String key, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(TEACHERS)
                .child(key)
                .setValue(teacher, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Teacher teacher, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(TEACHERS);
        FirebaseDatabase.getInstance()
                .getReference(TEACHERS)
                .child(teacher.getId())
                .updateChildren(teacher.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Teacher teacher, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(TEACHERS);
        FirebaseDatabase.getInstance()
                .getReference(TEACHERS)
                .child(teacher.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
