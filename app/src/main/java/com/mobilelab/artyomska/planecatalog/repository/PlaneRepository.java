package com.mobilelab.artyomska.planecatalog.repository;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.database.AppDatabase;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.model.User;

import java.util.ArrayList;
import java.util.List;

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

    public List<Plane> getAllPlanes()
    {
        return db.planeDao().getAllPlane();
    }

    public boolean insertPlane(Plane plane)
    {
        Plane foundPlane = db.planeDao().getPlaneByName(plane.getPlaneName());
        if (foundPlane != null)
            return false;
        long isInserted = db.planeDao().addPlane(plane);
        if (isInserted == 0)
            return false;
        else
            return true;
    }

    public void deletePlane(String planeName)
    {
        db.planeDao().removeAPlane(planeName);
    }

    public void updatePlane(Plane plane)
    {
        db.planeDao().updatePlane(plane);
    }
}
