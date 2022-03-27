package com.hevs.classroom_management_app.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity(tableName = "Reservation", primaryKeys = {"classroomId", "startTime", "endTime"},
        foreignKeys = {
                @ForeignKey(entity = Teacher.class, parentColumns = "id", childColumns = "teacherId"),
                @ForeignKey(entity = Classroom.class, parentColumns = "id", childColumns = "classroomId",
                        onDelete = ForeignKey.CASCADE)
        })
public class Reservation {

    @ColumnInfo(name = "classroomId")
    private long classroomId;
    @NonNull @ColumnInfo(name = "startTime")
    private LocalDateTime startTime;
    @NonNull @ColumnInfo(name = "endTime")
    private LocalDateTime endTime;
    @ColumnInfo(name = "teacherId")
    private long teacherId;

    @ColumnInfo
    private int occupantsNumber;
    @ColumnInfo
    private String reservationText;

    public Reservation(long classroomId, LocalDateTime startTime, LocalDateTime endTime, long teacherId, int occupantsNumber, String reservationText) {
        this.classroomId = classroomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherId = teacherId;
        this.occupantsNumber = occupantsNumber;
        this.reservationText = reservationText;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getOccupantsNumber() {
        return occupantsNumber;
    }

    public void setOccupantsNumber(int occupantsNumber) {
        this.occupantsNumber = occupantsNumber;
    }

    public String getReservationText() {
        return reservationText;
    }

    public void setReservationText(String reservationText) {
        this.reservationText = reservationText;
    }
}
