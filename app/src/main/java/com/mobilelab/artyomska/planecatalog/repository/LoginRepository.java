package com.mobilelab.artyomska.planecatalog.repository;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.database.AppDatabase;
import com.mobilelab.artyomska.planecatalog.model.User;

/**
 * Created by Artyomska on 10/16/2017.
 */

public class LoginRepository
{
    private AppDatabase db;

    public LoginRepository(Context dbContext)
    {
        this.db = AppDatabase.getDatabase(dbContext);
    }

    public boolean authenticateUser(User user)
    {
        User foundUser = db.userDao().getUserByName(user.getUsername());
        if (foundUser == null)
            return false;
        else
        {
            if (foundUser.getPassword().equals(user.getPassword()))
                return true;
            else
                return false;
        }
    }

    public boolean registerUser(User user)
    {
        User foundUser = db.userDao().getUserByName(user.getUsername());
        if (foundUser != null)
            return false;
        long isInserted = db.userDao().addUser(user);
        if (isInserted == 0)
            return false;
        else
            return true;
    }
}
