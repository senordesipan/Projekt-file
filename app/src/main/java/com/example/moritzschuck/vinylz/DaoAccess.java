package com.example.moritzschuck.vinylz;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

/**
 * Created by cip on 13.08.2018.
 */

@Dao
public interface DaoAccess {

    @Insert
    void insertPlatte(Platte platte);

    @Query("SELECT * FROM platte WHERE plattenID LIKE:plattenID")
    Platte findPlatteByID(String plattenID);

    @Query("SELECT * FROM platte WHERE title LIKE:title")
    Platte findPlatteByTitle(String title);
    //Wirkt so als würde es nicht funktionieren hier.


   /* @Query("SELECT * FROM Platte WHERE band LIKE band")
    Platte findPlatteByBand(String plattenID);
    /Kann man hier auch PlattenArrays mit allen Platten zurückgeben lassen, wenn ja, drop nen Kommentar ;)

    @Query("SELECT * FROM Platte WHERE year LIKE year")
    Platte findPlatteByYear(String plattenID);*/



}
