package com.hevs.classroom_management_app.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;

import java.time.LocalDateTime;
import java.util.List;

@Dao
public interface ReservationDao {
    @Query("SELECT * FROM reservation WHERE classroomID = :id")
    LiveData<List<Reservation>> getReservationsByClassId(long id);

    @Query("SELECT * FROM reservation WHERE classroomID = :id AND startTime=:startTime")
    LiveData<Reservation> getReservationsByClassAndStartTime(long id, LocalDateTime startTime);

    @Transaction
    @Query("SELECT * FROM reservation WHERE classroomId = :classroom_ID")
    LiveData<List<ReservationWithTeacher>> getReservationsByClassID(long classroom_ID);

    @Query("SELECT * FROM reservation WHERE teacherId=:teacherId")
    LiveData<List<Reservation>> getByTeacherId(long teacherId);

    @Insert
    void insert(Reservation reservation) throws SQLiteConstraintException;

    @Update
    void update(Reservation reservation);

    @Query("UPDATE reservation SET startTime = :newStartTime, endtime=:endTime, occupantsNumber=:occupantsNb, reservationText=:reservationText WHERE startTime=:originalStartTime AND classroomId=:classroomId")
    void update(long classroomId, LocalDateTime originalStartTime, LocalDateTime newStartTime,
                LocalDateTime endTime, int occupantsNb, String reservationText);

    @Delete
    void deleteByTeacherId(Reservation reservation);

    @Query("DELETE FROM reservation")
    void deleteAll();
}
