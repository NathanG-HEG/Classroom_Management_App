package com.hevs.classroom_management_app.database.firebase;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.hevs.classroom_management_app.database.entity.Classroom;

public class ClassroomLiveData extends LiveData<Classroom> {
    private final DatabaseReference reference;
    private final ClassroomLiveData.MyValueEventListener listener =
            new ClassroomLiveData.MyValueEventListener();

    public ClassroomLiveData(DatabaseReference ref){
        reference = ref;
    }

    @Override
    protected void onActive() {
        reference.addValueEventListener(listener);
    }

    private class MyValueEventListener implements ValueEventListener{
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot){
            Classroom classroom = dataSnapshot.getValue(Classroom.class);
            classroom.setId(dataSnapshot.getKey());
            setValue(classroom);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError){
            // Manage error
        }
    }
}
