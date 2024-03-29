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
    @Query("SELECT * FROM teacher WHERE email = :email AND digest = :digest")
    LiveData<Teacher> getByLogin(String email, String digest);

    @Query("SELECT * FROM teacher WHERE id = :id")
    LiveData<Teacher> getById(long id);

    @Query("SELECT * FROM teacher WHERE email = :email")
    LiveData<Teacher> getByEmail(String email);

    @Insert
    void insert(Teacher teacher) throws SQLiteConstraintException;

    @Update
    void update(Teacher teacher);

    @Delete
    void delete(Teacher teacher);

    @Query("DELETE FROM teacher")
    void deleteAll();
}
