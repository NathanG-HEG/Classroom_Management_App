package com.hevs.classroom_management_app.database.dao;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hevs.classroom_management_app.database.entity.Teacher;

@Dao
public interface TeacherDao {
    @Query("SELECT * FROM teacher WHERE email = :email AND password = :password")
    LiveData<Teacher> getByLogin(String email, String password);

    @Query("SELECT * FROM teacher WHERE id = :id")
    LiveData<Teacher> getById(long id);

    @Insert
    void insert(Teacher teacher) throws SQLiteConstraintException;

    @Update
    void update(Teacher teacher);

    @Delete
    void delete(Teacher teacher);

    @Query("DELETE FROM teacher")
    void deleteAll();
}
