package com.hevs.classroom_management_app.database.entity;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Teacher {

    private String id;
    private String lastname;
    private String firstname;
    private String email;
    private String password;

    public Teacher() {
    }

    public Teacher(String lastname, String firstname, String email, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        this.password = password;
    }

    public HashMap<String, Object> toMap(){
        HashMap<String, Object> res = new HashMap<>();
        res.put("lastName", lastname);
        res.put("firstName", firstname);
        return res;
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Teacher)) return false;
        Teacher o = (Teacher) obj;
        return o.getEmail().equals(this.getEmail());
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
