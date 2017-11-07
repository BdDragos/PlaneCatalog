package com.mobilelab.artyomska.planecatalog.controller;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.repository.LoginRepository;
import com.mobilelab.artyomska.planecatalog.repository.PlaneRepository;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class MainController
{
    PlaneRepository repo;

    public MainController(Context dbContext)
    {
        this.repo = new PlaneRepository(dbContext);
    }
}
