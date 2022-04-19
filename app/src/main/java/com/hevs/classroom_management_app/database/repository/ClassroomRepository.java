package com.hevs.classroom_management_app.database.repository;

import androidx.lifecycle.LiveData;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.firebase.ClassroomLiveData;
import com.hevs.classroom_management_app.database.firebase.ClassroomsListLiveData;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

import java.util.List;

public class ClassroomRepository {
    private final String CLASSROOMS = "classrooms";
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

    public LiveData<Classroom> getById(final String id) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(id);
        return new ClassroomLiveData(ref);
    }

    public LiveData<List<Classroom>> getAll () {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS);
        return new ClassroomsListLiveData(ref);
    }


    public void insert(final Classroom classroom, OnAsyncEventListener callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS);
        String key = ref.push().getKey();
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(key)
                .setValue(classroom, (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void update(final Classroom classroom, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS);
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(classroom.getId())
                .updateChildren(classroom.toMap(), (databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }

    public void delete(final Classroom classroom, OnAsyncEventListener callback) {
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS);
        FirebaseDatabase.getInstance()
                .getReference(CLASSROOMS)
                .child(classroom.getId())
                .removeValue((databaseError, databaseReference) -> {
                    if (databaseError != null) {
                        callback.onFailure(databaseError.toException());
                    } else {
                        callback.onSuccess();
                    }
                });
    }
}
