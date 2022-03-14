package com.hevs.classroom_management_app.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;

import java.util.List;

public class ClassRoomWithReservation {

    @Embedded
    public Classroom classroom;

    @Relation(parentColumn = "id", entityColumn = "classroomId", entity = Reservation.class)
    public List<Reservation> reservations;


}
