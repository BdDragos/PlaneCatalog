package com.mobilelab.artyomska.planecatalog.model.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.mobilelab.artyomska.planecatalog.model.User;

import java.util.List;

/**
 * Created by Artyomska on 10/17/2017.
 */

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long addUser(User user);

    @Query("select * from user")
    List<User> getAllUser();

    @Query("select * from user where username = :username")
    User getUserByName(String username);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateUser(User user);

    @Query("delete from user")
    void removeAllUsers();

    @Query("delete from user where username = :username")
    void removeAllUsers(String username);


}
