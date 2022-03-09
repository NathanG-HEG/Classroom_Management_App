package com.hevs.classroom_management_app.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;

@Entity(tableName = "Reservation", primaryKeys = {"classroomId", "startTime", "endTime"},
        foreignKeys = {
                @ForeignKey(entity = Teacher.class, parentColumns = "id", childColumns = "teacherId"), @ForeignKey(entity = Classroom.class, parentColumns = "id", childColumns = "classroomId")
        })
public class Reservation {

    @ColumnInfo(name = "classroomId")
    private long classroomId;
    @NonNull @ColumnInfo(name = "startTime")
    private Date startTime;
    @NonNull @ColumnInfo(name = "endTime")
    private Date endTime;
    @ColumnInfo(name = "teacherId")
    private long teacherId;

    @ColumnInfo
    private int occupantsNumber;

    public Reservation(long classroomId, Date startTime, Date endTime, long teacherId, int occupantsNumber) {
        this.classroomId = classroomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherId = teacherId;
        this.occupantsNumber = occupantsNumber;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Reservation)) return false;
        Reservation o = (Reservation) obj;
        return o.getClassroomId() == this.getClassroomId() && o.getStartTime() == this.getStartTime();
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    public long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(long classroomId) {
        this.classroomId = classroomId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getOccupantsNumber() {
        return occupantsNumber;
    }

    public void setOccupantsNumber(int occupantsNumber) {
        this.occupantsNumber = occupantsNumber;
    }
}
