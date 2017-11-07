package com.mobilelab.artyomska.planecatalog.database;

/**
 * Created by Artyomska on 10/17/2017.
 */

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.mobilelab.artyomska.planecatalog.model.Dao.PlaneDao;
import com.mobilelab.artyomska.planecatalog.model.Dao.UserDao;
import com.mobilelab.artyomska.planecatalog.model.Plane;
import com.mobilelab.artyomska.planecatalog.model.User;

@Database(entities = {User.class, Plane.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();

    public abstract PlaneDao planeDao();

    private static final Object sLock = new Object();

    static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(SupportSQLiteDatabase database)
        {

        }
    };

    public static AppDatabase getDatabase(Context context) {
        synchronized (sLock)
        {
            if (INSTANCE == null)
            {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ProgramDatabase")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
            return INSTANCE;
        }
    }

    public static void destroyInstance()
    {
        INSTANCE = null;
    }
}
