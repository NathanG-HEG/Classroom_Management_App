package com.hevs.classroom_management_app.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Classroom {

    private String id;
    private String name;
    private int capacity;

    public Classroom(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public Classroom() {
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> res = new HashMap<>();
        res.put("name", name);
        res.put("capacity", capacity);
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Classroom)) return false;
        Classroom o = (Classroom) obj;
        return o.getName().equals(this.getName());
    }
    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
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
