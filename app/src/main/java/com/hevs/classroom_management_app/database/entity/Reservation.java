package com.hevs.classroom_management_app.database.entity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import com.google.firebase.database.Exclude;
import com.hevs.classroom_management_app.database.LocalDateTimeConverter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Reservation {

    private String reservationId;
    private String classroomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String teacherId;
    private int occupantsNumber;
    private String reservationText;

    public Reservation(String classroomId, LocalDateTime startTime, LocalDateTime endTime, String teacherId, int occupantsNumber, String reservationText) {
        this.classroomId = classroomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherId = teacherId;
        this.occupantsNumber = occupantsNumber;
        this.reservationText = reservationText;
    }

    public Reservation() {

    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> res = new HashMap<>();
        res.put("classroomId", classroomId);
        res.put("startTime", LocalDateTimeConverter.toDateString(startTime));
        res.put("endTime", LocalDateTimeConverter.toDateString(endTime));
        res.put("teacherId", teacherId);
        res.put("occupants", occupantsNumber);
        res.put("reservationText", reservationText);
        return res;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Reservation)) return false;
        Reservation o = (Reservation) obj;
        return o.getClassroomId() == this.getClassroomId() && o.getStartTime() == this.getStartTime();
    }

    @Exclude
    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(String classroomId) {
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
