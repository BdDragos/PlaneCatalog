package com.mobilelab.artyomska.planecatalog.model.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyomska on 11/6/2017.
 */

@Dao
public interface PlaneDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long addPlane(Plane plane);

    @Query("select * from plane")
    List<Plane> getAllPlane();

    @Query("select * from plane where planeName = :planeName")
    Plane getPlaneByName(String planeName);

    @Query("select * from plane where planeEngine = :planeEngine")
    Plane getPlaneByEngine(String planeEngine);

    @Query("select * from plane where planeProducer = :planeProducer")
    Plane getPlaneByProducer(String planeProducer);

    @Query("select * from plane where planeCountry = :planeCountry")
    Plane getPlaneByCountry(String planeCountry);

    @Query("select * from plane where planeYear = :planeYear")
    Plane getPlaneByYear(int planeYear);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePlane(Plane plane);

    @Query("delete from plane")
    void removeAllPlane();

    @Query("delete from plane where planeName = :planeName")
    void removeAPlane(String planeName);

}