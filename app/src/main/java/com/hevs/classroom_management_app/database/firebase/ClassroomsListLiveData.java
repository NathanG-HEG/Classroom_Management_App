package com.hevs.classroom_management_app.database.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hevs.classroom_management_app.database.entity.Classroom;

import java.util.LinkedList;
import java.util.List;

public class ClassroomsListLiveData extends LiveData<List<Classroom>> {
    private final DatabaseReference reference;
    private final MyValueEventListener listener = new MyValueEventListener();

    public ClassroomsListLiveData(DatabaseReference ref) {
        reference = ref;
    }

    private class MyValueEventListener implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            setValue(toClassrooms(dataSnapshot));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            // Manage error
        }
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private List<Classroom> toClassrooms(DataSnapshot snapshot){
        List<Classroom> classrooms = new LinkedList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren()){
            Classroom classroom = childSnapshot.getValue(Classroom.class);
            classroom.setId(childSnapshot.getKey());
            classrooms.add(classroom);
        }
        return classrooms;
    }
}