package com.example.moritzschuck.vinylz;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Platte.class}, version = 1, exportSchema = false)

public abstract class PlatteDatabase  extends RoomDatabase{

    public abstract DaoAccess daoAccess();
}
