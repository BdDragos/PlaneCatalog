package com.mobilelab.artyomska.planecatalog.controller;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.repository.LoginRepository;
import com.mobilelab.artyomska.planecatalog.repository.PlaneRepository;

import java.util.ArrayList;
import java.util.List;

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

    public List<Plane> gettAllPlane()
    {
        return repo.getAllPlanes();
    }

    public boolean addNewPlane(Plane plane)
    {
        return repo.insertPlane(plane);
    }

    public void updatePlane(Plane plane)
    {
        repo.updatePlane(plane);
    }

    public void deletePlane(Plane plane)
    {
        repo.deletePlane(plane.getPlaneName());
    }
}
