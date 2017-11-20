package com.mobilelab.artyomska.planecatalog.service;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.model.User;
import com.mobilelab.artyomska.planecatalog.repository.LoginRepository;

/**
 * Created by Artyomska on 10/16/2017.
 */

public class LoginService
{
    LoginRepository repo;

    public LoginService(Context dbContext)
    {
        this.repo = new LoginRepository(dbContext);
    }

    public boolean isAuthenticated(User user)
    {
        if (repo.authenticateUser(user))
            return true;
        else
            return false;
    }

    public boolean isRegistered(User user)
    {
        if (repo.registerUser(user))
            return true;
        else
            return false;
    }

}
