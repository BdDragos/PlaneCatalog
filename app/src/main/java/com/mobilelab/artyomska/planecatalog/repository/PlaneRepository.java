package com.mobilelab.artyomska.planecatalog.repository;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.database.AppDatabase;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class PlaneRepository
{
    private AppDatabase db;

    public PlaneRepository(Context dbContext)
    {
        this.db = AppDatabase.getDatabase(dbContext);
    }
}
