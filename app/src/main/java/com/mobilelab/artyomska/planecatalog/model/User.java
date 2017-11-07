package com.mobilelab.artyomska.planecatalog.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Artyomska on 10/16/2017.
 */
@Entity(indices={@Index(value="username", unique=true)})
public class User
{
    @PrimaryKey(autoGenerate = true) @NotNull
    private int ID;
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @NotNull
    public int getID() {
        return ID;
    }

    public void setID(@NotNull int ID) {
        this.ID = ID;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User()
    {

    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
