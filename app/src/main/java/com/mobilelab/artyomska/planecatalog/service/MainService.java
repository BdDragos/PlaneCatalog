package com.mobilelab.artyomska.planecatalog.service;

import android.content.Context;

import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.repository.PlaneRepository;

import java.util.List;

/**
 * Created by Artyomska on 11/6/2017.
 */

public class MainService
{
    PlaneRepository repo;

    public MainService(Context dbContext)
    {
        this.repo = new PlaneRepository(dbContext);
    }

    public List<Plane> gettAllPlane()
    {
        return repo.getAllPlanes();
    }

    public boolean addNewPlane(String name, String engine, String producer, String country, int year, String wiki)
    {
        Plane plane = new Plane(name,engine,producer,country,year,wiki);
        return repo.insertPlane(plane);
    }

    public boolean updatePlane(Plane plane)
    {
        return repo.updatePlane(plane);
    }

    public void deletePlane(String planeName)
    {
        repo.deletePlane(planeName);
    }

}
