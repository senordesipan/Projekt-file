package com.example.moritzschuck.vinylz;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface DaoAccess {

    @Insert
    long insertPlatte(Platte platte);

    @Query("SELECT * FROM platte WHERE plattenID LIKE:plattenID")
    Platte findPlatteByID(long plattenID);

    @Query("SELECT * FROM platte WHERE title  LIKE :title")
    Platte findPlatteByTitle(String title);
    //Wirkt so als würde es nicht funktionieren hier.

    @Query("SELECT * FROM platte WHERE location = :location")
    Platte findPlatteByLocation(String location);

    @Query("SELECT * FROM platte WHERE fav LIKE :fav")
    List<Platte> findFavs(boolean fav);

    @Query("SELECT * FROM platte WHERE fav LIKE :fav AND title LIKE :title")
    Platte findFavTitle(boolean fav, String title);

    @Query("SELECT * FROM platte")
    List<Platte> selectAll();

    @Delete
    void deletePlatte(Platte platte);



   /* @Query("SELECT * FROM Platte WHERE band LIKE band")
    Platte findPlatteByBand(String plattenID);
    /Kann man hier auch PlattenArrays mit allen Platten zurückgeben lassen, wenn ja, drop nen Kommentar ;)

    @Query("SELECT * FROM Platte WHERE year LIKE year")
    Platte findPlatteByYear(String plattenID);*/



}
