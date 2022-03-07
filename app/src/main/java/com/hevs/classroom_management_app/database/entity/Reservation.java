package com.hevs.classroom_management_app.database.entity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "Reservation", primaryKeys = {"classId", "startTime", "endTime"},
        foreignKeys = {
                @ForeignKey(entity = Teacher.class, parentColumns = "id", childColumns = "teacherId"), @ForeignKey(entity = Classroom.class, parentColumns = "id", childColumns = "classroomId")
        })
public class Reservation {

    @ColumnInfo(name = "classID")
    private long classId;
    @ColumnInfo(name = "startTime")
    private Date startTime;
    @ColumnInfo(name = "endTime")
    private Date endTime;

    @ColumnInfo
    private int occupantsNumber;

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Reservation)) return false;
        Reservation o = (Reservation) obj;
        return o.getClassId() == this.getClassId() && o.getStartTime() == this.getStartTime();
    }

    public long getClassId() {
        return classId;
    }

    public void setClassId(long classId) {
        this.classId = classId;
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
