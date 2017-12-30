package com.mobilelab.artyomska.planecatalog.service;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.model.User;
import com.mobilelab.artyomska.planecatalog.repository.LoginRepository;
import com.mobilelab.artyomska.planecatalog.repository.PlaneRepository;

/**
 * Created by Artyomska on 12/30/2017.
 */

public class LoginService
{
    private LoginRepository repo;

    public LoginService(Context dbContext)
    {
        this.repo = new LoginRepository(dbContext);
    }

    public boolean authenticateUser(User user)
    {
        return repo.authenticateUser(user);
    }

    public boolean registerUser(User user)
    {
        return repo.registerUser(user);
    }
}
