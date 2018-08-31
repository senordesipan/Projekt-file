package com.example.moritzschuck.vinylz;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {Platte.class}, version = 2, exportSchema = false)
public abstract class PlatteDatabase extends RoomDatabase{
    private static final String DB_NAME = "platteDB";
    private static volatile PlatteDatabase instance;

    static synchronized PlatteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public PlatteDatabase() {}

    private static PlatteDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                PlatteDatabase.class,
                DB_NAME

        ).fallbackToDestructiveMigration().build();

    }

    public abstract DaoAccess daoAccess();
}