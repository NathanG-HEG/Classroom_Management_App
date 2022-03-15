package com.hevs.classroom_management_app.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.entity.Teacher;

import java.util.List;

public class ClassroomWithReservations {

    @Embedded
    public Classroom classroom;

    @Relation(parentColumn = "id", entityColumn = "classroomId", entity = Reservation.class)
    public List<Reservation> reservations;

    @Relation(parentColumn = "id", entityColumn = "teacherId", entity = Teacher.class)
    public Teacher teacher;


}
