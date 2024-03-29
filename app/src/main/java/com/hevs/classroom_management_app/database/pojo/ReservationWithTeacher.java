package com.hevs.classroom_management_app.database.pojo;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.entity.Teacher;

import java.util.List;

public class ReservationWithTeacher {
    @Embedded
    public Reservation reservation;

    @Relation(parentColumn = "teacherId", entityColumn = "id", entity = Teacher.class)
    public Teacher teacher;
}
