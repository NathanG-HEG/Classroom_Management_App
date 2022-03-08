package com.hevs.classroom_management_app.database.dao;

import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.entity.Teacher;

import java.util.List;

@Dao
public interface ClassroomDao {
    @Query("SELECT * FROM classroom WHERE id = :id")
    LiveData<Classroom> getById(long id);

    @Query("SELECT * FROM classroom WHERE name = :name")
    LiveData<Classroom> getByName(String name);

    @Query("SELECT * FROM classroom")
    LiveData<List<Classroom>> getAll();

    @Insert
    void insert(Classroom classroom) throws SQLiteConstraintException;

    @Update
    void update(Classroom classroom);

    @Delete
    void delete(Classroom classroom);

    @Query("DELETE FROM classroom")
    void deleteAll();
}
