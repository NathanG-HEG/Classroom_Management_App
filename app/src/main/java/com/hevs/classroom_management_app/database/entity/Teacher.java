package com.hevs.classroom_management_app.database.entity;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Entity(tableName = "Teacher", indices = {@Index(value = {"email"}, unique = true)})
public class Teacher {

    @PrimaryKey (autoGenerate = true)
    private long id;
    @ColumnInfo
    private String lastname;
    @ColumnInfo
    private String firstname;
    @ColumnInfo
    private String email;
    @ColumnInfo
    private String digest;
    @ColumnInfo
    private String salt;

    public Teacher() {
    }

    public Teacher(String lastname, String firstname, String email, String password) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.email = email;
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        this.salt = new String(salt);
        this.digest = hash(password, this.salt);

    }

    private String hash(String text, String salt){
        byte[] s = salt.getBytes(StandardCharsets.UTF_8);
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(s);
        return new String(md.digest(text.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Teacher)) return false;
        Teacher o = (Teacher) obj;
        return o.getEmail().equals(this.getEmail());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
}
