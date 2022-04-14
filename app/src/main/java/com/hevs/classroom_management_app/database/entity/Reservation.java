package com.hevs.classroom_management_app.database.entity;

import androidx.annotation.Nullable;

import com.google.firebase.database.Exclude;
import com.hevs.classroom_management_app.database.LocalDateTimeConverter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Reservation {

    private String reservationId;
    private String classroomId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String teacherName;
    private int occupantsNumber;
    private String reservationText;

    public Reservation(String classroomId, LocalDateTime startTime, LocalDateTime endTime, String teacherName, int occupantsNumber, String reservationText) {
        this.classroomId = classroomId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.teacherName = teacherName;
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
        res.put("teacherName", teacherName);
        res.put("occupantsNumber", occupantsNumber);
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public void setStartTime(String startTime) {
        this.startTime = LocalDateTimeConverter.toDate(startTime);
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = LocalDateTimeConverter.toDate(endTime);
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
