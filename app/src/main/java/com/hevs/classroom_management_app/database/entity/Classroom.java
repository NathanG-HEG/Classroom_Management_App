package com.hevs.classroom_management_app.database.entity;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "Classroom")
public class Classroom {

    @PrimaryKey (autoGenerate = true)
    private long id;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private int capacity;

    public Classroom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Classroom)) return false;
        Classroom o = (Classroom) obj;
        return o.getName().equals(this.getName());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
